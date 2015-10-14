package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

@ClassGeneratorFeatures(name = "Table", parameterName = "Table")
public class TableGenerator extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.semantic.Table mTable;
    private RowGenerator mRowGenerator;
    private PrimaryKeyGenerator mPrimaryKeyGenerator;

    public TableGenerator(
            JPackage jPackage,
            com.robwilliamson.healthyesther.semantic.Table table) throws JClassAlreadyExistsException {
        mTable = table;
        setJClass(jPackage._class(JMod.PUBLIC | JMod.FINAL, getName()));
        getJClass()._extends(Table.class);
        getJClass()._implements(Serializable.class);
        makeColumnNameFields();
        makeGetName();
        makeCreate();
        makeDrop();
    }

    private void makeColumnNameFields() {
        JDefinedClass clazz = getJClass();
        List<Column> columns = Arrays.asList(mTable.getColumns());
        Collections.sort(columns, new Column.Comparator());
        for (Column column : columns) {
            clazz.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, column.getName().toUpperCase(), JExpr.lit(column.getName()));
        }
    }

    private void makeGetName() {
        JMethod getName = getJClass().method(JMod.PUBLIC, String.class, "getName");
        getName.annotate(Nonnull.class);
        getName.annotate(Override.class);
        getName.body()._return(JExpr.lit(mTable.getName()));
    }

    private void makeDrop() {
        JMethod drop = getJClass().method(JMod.PUBLIC, model().VOID, "drop");
        JVar transaction = drop.param(Transaction.class, "transaction");
        drop.annotate(Override.class);
        drop.body().invoke(transaction, "execSQL").arg("DROP TABLE IF EXISTS " + mTable.getName());
    }

    @Override
    public String getName() {
        return Strings.capitalize(Strings.underscoresToCamel(mTable.getName())) +
                Strings.capitalize(super.getName());
    }

    @Override
    public String getPreferredParameterName() {
        return Strings.lowerCase(Strings.underscoresToCamel(mTable.getName())) +
                Strings.capitalize(super.getPreferredParameterName());
    }

    public com.robwilliamson.healthyesther.semantic.Table getTable() {
        return mTable;
    }

    public RowGenerator getRow() {
        return mRowGenerator;
    }

    public void init() throws JClassAlreadyExistsException {
        mRowGenerator = new RowGenerator(this);

        mPrimaryKeyGenerator = new PrimaryKeyGenerator(this);

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                mRowGenerator.init();
                makeSelect();
            }
        });
    }

    private void makeSelect() {
        JDefinedClass rowClass = getRow().getJClass();
        JType whereClass = model()._ref(Where.class);
        JMethod select = makeSelect(whereClass);

        JVar db = select.params().get(0);
        JVar where = select.params().get(1);

        JBlock body = select.body();
        JVar cursor = body.decl(JMod.FINAL, model()._ref(Cursor.class), "cursor", db.invoke("select").arg(where).arg(JExpr._this()));
        JVar rows = body.decl(JMod.FINAL, rowClass.array(), "rows", JExpr.newArray(rowClass, cursor.invoke("count")));
        JVar index = body.decl(model().INT, "index", JExpr.lit(0));
        JBlock moreThan1 = body._if(cursor.invoke("count").gt(JExpr.lit(0)))._then();
        moreThan1.invoke(cursor, "moveToFirst");

        JBlock loop = moreThan1._do(cursor.invoke("moveToNext")).body();
        JInvocation rowConstruction = JExpr._new(rowClass);
        rowConstruction.arg(cursor);
        loop.assign(rows.component(index.incr()), rowConstruction);

        select.body()._return(rows);

        JMethod selectOverload = makeSelect(getPrimaryKeyGenerator().getJClass());

        db = selectOverload.params().get(0);
        where = selectOverload.params().get(1);

        selectOverload.body()._return(JExpr.invoke(null, select).arg(db).arg(JExpr.cast(whereClass, where)));
    }

    private JMethod makeSelect(JType whereType) {
        JDefinedClass rowClass = getRow().getJClass();
        JMethod select = getJClass().method(JMod.PUBLIC, rowClass.array(), "select");
        select.annotate(Nonnull.class);
        JVar db = select.param(com.robwilliamson.healthyesther.db.includes.Database.class, "database");
        db.annotate(Nonnull.class);
        JVar where = select.param(whereType, "where");
        where.annotate(Nonnull.class);
        return select;
    }

    private void makeCreate() {
        JMethod create = getJClass().method(JMod.PUBLIC, model().VOID, "create");
        JVar transaction = create.param(Transaction.class, "transaction");
        create.annotate(Override.class);
        create.body().invoke(transaction,
                "execSQL").arg(JExpr.lit(mTable.getDdl()));
    }

    public PrimaryKeyGenerator getPrimaryKeyGenerator() {
        return mPrimaryKeyGenerator;
    }
}
