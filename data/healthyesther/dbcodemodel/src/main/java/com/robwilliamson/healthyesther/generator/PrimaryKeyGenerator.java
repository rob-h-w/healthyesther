package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
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
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "PrimaryKey", parameterName = "PrimaryKey")
public class PrimaryKeyGenerator extends BaseClassGenerator {
    private final TableGenerator mTableGenerator;
    private List<ColumnField> mSortedPrimaryKeyFields;
    private JMethod mCopyConstructor;
    private JMethod mValueConstructor;

    public PrimaryKeyGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super();

        mTableGenerator = tableGenerator;

        setJClass(tableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                makePrimaryKeyValues();
                makeConstructors();
                makeValueAccessors();
                makeEquals();
            }
        });
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

            JMethod getter = getJClass().method(
                    JMod.PUBLIC,
                    field.fieldVar.type(),
                    "get" + Strings.capitalize(field.name));
            getter.body()._return(field.fieldVar);
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

    public boolean hasPrimaryKeys() {
        return !mSortedPrimaryKeyFields.isEmpty();
    }

    @Override
    public String getName() {
        return getTableGenerator().getName() + Strings.capitalize(super.getName());
    }

    @Override
    public String getPreferredParameterName() {
        return getTableGenerator().getPreferredParameterName() + super.getPreferredParameterName();
    }

    private void makePrimaryKeyValues() {
        final ColumnField.Maker def = new ColumnField.DefaultMaker(getJClass());
        Map<String, ColumnField> fieldMap = ColumnField.makeMap(getPrimaryKeyColumns(), new ColumnField.Maker() {
            @Override
            public ColumnField make(Column column) {
                if (column.isForeignKey()) {
                    return def.make(column);
                }

                JType type = column.getPrimitiveType(model());
                JFieldVar fieldVar = getJClass().field(JMod.PRIVATE, type, ColumnField.memberName(column.getName()));
                return new ColumnField(fieldVar, column);
            }
        });
        mSortedPrimaryKeyFields = ColumnField.makeSortedList(fieldMap);
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
        JCodeModel model = theClass.owner();
        JBlock equals = eqMethod.body();
        JVar other = eqMethod.listParams()[0];

        if (!mSortedPrimaryKeyFields.isEmpty()) {
            // Cast
            JVar otherType = equals.decl(theClass, "the" + theClass.name(), JExpr.cast(theClass, other));

            // Check each primary key column.
            for (ColumnField field : mSortedPrimaryKeyFields) {
                JFieldVar primaryKeyField = field.fieldVar;
                JBlock ifBlock = equals._if(otherType.ref(primaryKeyField).ne(primaryKeyField))._then();

                if (primaryKeyField.type().isPrimitive()) {
                    ifBlock._return(JExpr.lit(false));
                } else {
                    // Check nullness.
                    // if (otherType.key == null || !otherType.key.equals(key) { return false }
                    ifBlock = ifBlock._if(otherType.ref(primaryKeyField).eq(JExpr._null()).cor(otherType.ref(primaryKeyField).invoke("equals").arg(primaryKeyField).not()))._then();
                    ifBlock._return(JExpr.lit(false));
                }
            }
        }

        // Finally return true
        equals._return(JExpr.lit(true));
    }
}
