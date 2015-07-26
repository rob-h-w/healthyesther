package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "PrimaryKey", parameterName = "PrimaryKey")
public class PrimaryKeyGenerator extends BaseClassGenerator {
    private class Field {
        public final String name;
        public final JFieldVar fieldVar;
        public final Column column;

        public Field(String name, JFieldVar fieldVar, Column column) {
            this.name = name;
            this.fieldVar = fieldVar;
            this.column = column;
        }
    }

    private final TableGenerator mTableGenerator;
    private final Map<String, Field> mPrimaryKeyFields;
    private final List<Field> mSortedPrimaryKeyFields;
    private JMethod mCopyConstructor;
    private JMethod mValueConstructor;

    public PrimaryKeyGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super();

        mTableGenerator = tableGenerator;
        mPrimaryKeyFields = new HashMap<>();
        mSortedPrimaryKeyFields = new ArrayList<>();

        setJClass(tableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                makePrimaryKeyValues();
                makeConstructors();
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

        for (Field field : mSortedPrimaryKeyFields) {
            mCopyConstructor.body().assign(field.fieldVar, JExpr.ref(other, field.fieldVar));
            JVar param = mValueConstructor.param(field.fieldVar.type(), field.name);
            mValueConstructor.body().assign(field.fieldVar, param);
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

    @Override
    public String getName() {
        return getTableGenerator().getName() + super.getName();
    }

    @Override
    public String getPreferredParameterName() {
        return getTableGenerator().getPreferredParameterName() + super.getPreferredParameterName();
    }

    private void makePrimaryKeyValues() {
        List <Column> primaryKeyColumns = getPrimaryKeyColumns();
        for (Column column : primaryKeyColumns) {
            JType type = column.getJtype(model());
            if (column.isForeignKey()) {
                type = column.getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
            }
            String name = Strings.capitalize(Strings.underscoresToCamel(column.getName()));
            JFieldVar primaryKeyField = getJClass().field(JMod.PRIVATE, type, "m" + name);
            Field field = new Field(name, primaryKeyField, column);
            mPrimaryKeyFields.put(column.getName(), field);
        }

        String[] names = new String[mPrimaryKeyFields.size()];
        mPrimaryKeyFields.keySet().toArray(names);
        Arrays.sort(names);

        for (String name : names) {
            mSortedPrimaryKeyFields.add(mPrimaryKeyFields.get(name));
        }
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
        JDefinedClass theClass = getJClass();
        JCodeModel model = theClass.owner();
        JMethod eqMethod = theClass.method(JMod.PUBLIC, model.BOOLEAN, "equals");
        JBlock equals = eqMethod.body();
        JVar other = eqMethod.param(model.ref(Object.class), "other");

        // If other == null
        JBlock ifBlock = equals._if(other.eq(JExpr._null()))._then();
        ifBlock._return(JExpr.lit(false));

        // If other == this
        ifBlock = equals._if(other.eq(JExpr._this()))._then();
        ifBlock._return(JExpr.lit(true));

        // Check type
        ifBlock = equals._if(other._instanceof(theClass).not())._then();
        ifBlock._return(JExpr.lit(false));

        if (!mSortedPrimaryKeyFields.isEmpty()) {
            // Cast
            JVar otherType = equals.decl(theClass, "the" + theClass.name(), JExpr.cast(theClass, other));

            // Check each primary key column.
            for (Field field : mSortedPrimaryKeyFields) {
                JFieldVar primaryKeyField = field.fieldVar;
                ifBlock = equals._if(otherType.ref(primaryKeyField).ne(primaryKeyField))._then();

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
