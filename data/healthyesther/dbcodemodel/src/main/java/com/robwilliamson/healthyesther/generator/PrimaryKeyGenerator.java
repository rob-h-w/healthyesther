package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.semantic.ColumnField;
import com.robwilliamson.healthyesther.semantic.Table;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "PrimaryKey", parameterName = "PrimaryKey")
public class PrimaryKeyGenerator extends BaseClassGenerator {
    private final TableGenerator mTableGenerator;
    private List<ColumnField> mSortedPrimaryKeyFields;
    private ColumnField mRowId;
    private Map<Column, JMethod> mGetters = new HashMap<>();
    private Map<Column, JMethod> mSetters = new HashMap<>();
    private JMethod mCopyConstructor;
    private JMethod mValueConstructor;

    public PrimaryKeyGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super();

        mTableGenerator = tableGenerator;

        setJClass(tableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
        getJClass()._implements(Key.class);
        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                makePrimaryKeyValues();
                makeConstructors();
                makeValueAccessors();
                makeEquals();
                implementWhere();
            }
        });
    }

    public static String getName(TableGenerator tableGenerator) {
        return Strings.capitalize(getName(PrimaryKeyGenerator.class));
    }

    private void implementWhere() {
        JMethod getWhere = getJClass().method(JMod.PUBLIC, String.class, "getWhere");
        getWhere.annotate(Override.class);
        JBlock body = getWhere.body();
        JVar where = body.decl(model()._ref(StringBuilder.class), "where", JExpr._new(model()._ref(StringBuilder.class)));
        String separator = "";

        for (ColumnField field : mSortedPrimaryKeyFields) {
            body.invoke(where, "append").arg(JExpr.lit(separator + "(" + field.column.getName() + " = "));
            if (field.column.isForeignKey()) {
                body.invoke(where, "append").arg(field.fieldVar.invoke("getId"));
            } else {
                body.invoke(where, "append").arg(field.fieldVar);
            }
            body.invoke(where, "append").arg(JExpr.lit(")"));

            separator = " AND ";
        }

        body._return(where.invoke("toString"));
    }

    public boolean hasRowId() {
        return mRowId != null;
    }

    public ColumnField getRowId() {
        return mRowId;
    }

    private void makeConstructors() {
        if (mSortedPrimaryKeyFields.isEmpty()) {
            return;
        }

        mCopyConstructor = getJClass().constructor(JMod.PUBLIC);
        mValueConstructor = getJClass().constructor(JMod.PUBLIC);

        JVar other = mCopyConstructor.param(getJClass(), "other");

        for (ColumnField field : mSortedPrimaryKeyFields) {
            mCopyConstructor.body().assign(field.fieldVar, JExpr.ref(other, field.fieldVar));
            JVar param = mValueConstructor.param(field.fieldVar.type(), field.name);
            mValueConstructor.body().assign(field.fieldVar, param);
        }
    }

    private void makeValueAccessors() {
        for (ColumnField field : mSortedPrimaryKeyFields) {
            JMethod setter = getJClass().method(
                    JMod.PUBLIC,
                    getJClass().owner().VOID,
                    "set" + Strings.capitalize(field.name));
            JVar value = setter.param(field.fieldVar.type(), field.name);
            setter.body().assign(field.fieldVar, value);
            mSetters.put(field.column, setter);

            JMethod getter = getJClass().method(
                    JMod.PUBLIC,
                    field.fieldVar.type(),
                    "get" + Strings.capitalize(field.name));
            getter.body()._return(field.fieldVar);
            mGetters.put(field.column, getter);
        }
    }

    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    public JMethod getCopyConstructor() {
        return mCopyConstructor;
    }

    public JMethod getValueConstructor() {
        return mValueConstructor;
    }

    public JMethod getGetterFor(Column column) {
        return mGetters.get(column);
    }

    public JMethod getRowIdGetter() {
        return getGetterFor(getRowId().column);
    }

    public JMethod getSetterFor(Column column) {
        return mSetters.get(column);
    }

    public boolean hasPrimaryKeys() {
        return !mSortedPrimaryKeyFields.isEmpty();
    }

    @Override
    public String getName() {
        return getName(getTableGenerator());
    }

    @Override
    public String getPreferredParameterName() {
        return getTableGenerator().getPreferredParameterName() + super.getPreferredParameterName();
    }

    private void makePrimaryKeyValues() {
        Column.Picker picker = new Column.AllPicker();
        final ColumnField.Maker def = new ColumnField.DefaultMaker(getJClass());
        Map<String, ColumnField> fieldMap = ColumnField.makeMap(getPrimaryKeyColumns(),
                new ColumnField.Maker() {
                    @Override
                    public ColumnField make(Column column) {
                        if (column.isForeignKey()) {
                            return def.make(column);
                        }

                        JType type = column.getPrimitiveType(model());
                        JFieldVar fieldVar = getJClass().field(JMod.PRIVATE, type, ColumnField.memberName(column.getName()));
                        ColumnField columnField = new ColumnField(fieldVar, column);

                        if (column.isRowId()) {
                            mRowId = columnField;
                        }

                        return columnField;
                    }
                },
                picker);
        mSortedPrimaryKeyFields = ColumnField.makeSortedList(fieldMap, picker);
    }

    private List<Column> getPrimaryKeyColumns() {
        List<Column> primaryKeyColumns = new ArrayList<>();

        Table table = getTableGenerator().getTable();

        for (Column column : table.getColumns()) {
            if (table.isPrimaryKey(column) || column.isPrimaryKey()) {
                primaryKeyColumns.add(column);
            }
        }

        return primaryKeyColumns;
    }

    private void makeEquals() {
        JMethod eqMethod = makeBasicEquals();
        JDefinedClass theClass = getJClass();
        JBlock equals = eqMethod.body();
        JVar other = eqMethod.listParams()[0];

        if (!mSortedPrimaryKeyFields.isEmpty()) {
            // Cast
            JVar otherType = equals.decl(theClass, "the" + theClass.name(), JExpr.cast(theClass, other));

            // Check each primary key column.
            for (ColumnField field : mSortedPrimaryKeyFields) {
                JFieldVar primaryKeyField = field.fieldVar;
                equals._if(makeEquals(primaryKeyField, otherType.ref(primaryKeyField)).not())._then()._return(JExpr.lit(false));
            }
        }

        // Finally return true
        equals._return(JExpr.lit(true));
    }
}
