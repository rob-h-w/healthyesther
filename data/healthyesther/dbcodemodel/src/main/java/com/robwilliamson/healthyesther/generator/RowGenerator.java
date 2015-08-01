package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.ColumnField;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public class RowGenerator extends BaseClassGenerator {

    private static String name(RowGenerator rowGenerator) {
        return Strings.lowerCase(rowGenerator.getTableGenerator().getPreferredParameterName()) + "Row";
    }

    private final TableGenerator mTableGenerator;
    private List<ColumnField> mSortedFields;
    private final JMethod mRowConstructor;
    private final JMethod mJoinConstructor;

    public RowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        mTableGenerator = tableGenerator;

        setJClass(mTableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));

        mRowConstructor = getJClass().constructor(JMod.PUBLIC);

        if (getTableGenerator().getTable().hasDependencies()) {
            mJoinConstructor = getJClass().constructor(JMod.PUBLIC);
        } else {
            mJoinConstructor = null;
        }
    }

    public void init() {
        mSortedFields = ColumnField.makeSortedList(getJClass(),
                Arrays.asList(getTableGenerator().getTable().getColumns()));
        initConstructors();
        makeAccessors();
    }

    private void initConstructors() {
        List<ColumnField> nullable = new ArrayList<>();
        List<ColumnField> nonNull = new ArrayList<>();

        for (ColumnField field : mSortedFields) {
            if (field.column.isNotNull()) {
                nonNull.add(field);
            } else {
                nullable.add(field);
            }
        }

        for (ColumnField field : nonNull) {
            if (field.column.isForeignKey()) {
                // Always there is a join constructor if there are foreign keys.
                addConstructorParam(field.column.getColumnDependency());
            } else if (field.column.isPrimaryKey()) {
                PrimaryKeyGenerator keyGenerator = field.column.getTable().getGenerator().getPrimaryKeyGenerator();
                mRowConstructor.param(keyGenerator.getJClass(), field.name);
                if (mJoinConstructor != null) {
                    mJoinConstructor.param(keyGenerator.getJClass(), field.name);
                }
            } else {
                mRowConstructor.param(field.column.getDependentJtype(model()), field.name);
                if (mJoinConstructor != null) {
                    mJoinConstructor.param(field.column.getDependentJtype(model()), field.name);
                }
            }
        }

        for (ColumnField field : nullable) {
            if (field.column.isForeignKey()) {
                addConstructorParam(field.column.getColumnDependency());
            } else {
                mRowConstructor.param(field.column.getDependentNullableJtype(model()), field.name);
                if (mJoinConstructor != null) {
                    mJoinConstructor.param(field.column.getDependentNullableJtype(model()), field.name);
                }
            }
        }
    }

    private void makeAccessors() {
        for (ColumnField field : mSortedFields) {
            JMethod setter = getJClass().method(
                    JMod.PUBLIC,
                    model().VOID,
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

    private void addConstructorParam(ColumnDependency columnDependency) {
        RowGenerator rowGenerator = columnDependency.getDependency().getTable().getGenerator().getRow();
        mRowConstructor.param(rowGenerator.getJClass(), name(rowGenerator));

        com.robwilliamson.healthyesther.semantic.Table table = rowGenerator.getTableGenerator().getTable();
        PrimaryKeyGenerator primaryKeyGenerator = table.getGenerator().getPrimaryKeyGenerator();
        mJoinConstructor.param(primaryKeyGenerator.getJClass(), primaryKeyGenerator.getPreferredParameterName());
    }
}
