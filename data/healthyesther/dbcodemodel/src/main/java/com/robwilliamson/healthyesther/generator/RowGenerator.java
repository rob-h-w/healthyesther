package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public class RowGenerator extends BaseClassGenerator {

    private static String name(Column column) {
        return Strings.underscoresToCamel(column.getTable().getName()) + Strings.capitalize(Strings.underscoresToCamel(column.getName()));
    }

    private static String name(RowGenerator rowGenerator) {
        return Strings.lowerCase(rowGenerator.getTableGenerator().getPreferredParameterName()) + "Row";
    }

    private final TableGenerator mTableGenerator;
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
        initConstructors();
    }

    private void initConstructors() {
        List<Column> nullable = new ArrayList<>();
        List<Column> nonNull = new ArrayList<>();

        for (Column column : getTableGenerator().getTable().getColumns()) {
            if (column.isNotNull()) {
                nonNull.add(column);
            } else {
                nullable.add(column);
            }
        }

        Column.Comparator comparator = new Column.Comparator();
        Collections.sort(nullable, comparator);
        Collections.sort(nonNull, comparator);

        for (Column column : nonNull) {
            if (column.isForeignKey()) {
                addConstructorParam(column.getColumnDependency());
            } else {
                mRowConstructor.param(primitiveType(column), name(column));
                if (mJoinConstructor != null) {
                    mJoinConstructor.param(primitiveType(column), name(column));
                }
            }
        }

        for (Column column : nullable) {
            if (column.isForeignKey()) {
                addConstructorParam(column.getColumnDependency());
            } else {
                mRowConstructor.param(nullableType(column), name(column));
                if (mJoinConstructor != null) {
                    mJoinConstructor.param(nullableType(column), name(column));
                }
            }
        }
    }

    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    protected JType primitiveType(Column column) {
        String typeName = column.getType();
        switch (typeName) {
            case "BOOLEAN":
                return mTableGenerator.model().BOOLEAN;
            case "INTEGER":
                return mTableGenerator.model().LONG;
            case "DATETIME":
            case "TEXT":
                return mTableGenerator.model().ref(String.class);
            case "REAL":
                return mTableGenerator.model().DOUBLE;
            default:
                return handleUnknownColumnType(column, mTableGenerator.model().LONG);
        }
    }

    protected JType nullableType(Column column) {
        String typeName = column.getType();
        switch (typeName) {
            case "BOOLEAN":
                return mTableGenerator.model().ref(Boolean.class);
            case "INTEGER":
                return mTableGenerator.model().ref(Long.class);
            case "DATETIME":
            case "TEXT":
                return mTableGenerator.model().ref(String.class);
            case "REAL":
                return mTableGenerator.model().ref(Double.class);
            default:
                return handleUnknownColumnType(column, mTableGenerator.model().ref(Long.class));
        }
    }

    private JType handleUnknownColumnType(Column column, JType indexer) {
        String typeName = column.getType();

        if (Strings.isEmpty(typeName)) {
            if (column.isForeignKey()) {
                return indexer;
            }
        }

        throw new IllegalArgumentException("Type " + typeName + " is unsupported.");
    }

    private void addConstructorParam(ColumnDependency columnDependency) {
        RowGenerator rowGenerator = columnDependency.getDependency().getTable().getGenerator().getRow();
        mRowConstructor.param(rowGenerator.getJClass(), name(rowGenerator));

        com.robwilliamson.healthyesther.semantic.Table table = rowGenerator.getTableGenerator().getTable();
        PrimaryKeyGenerator primaryKeyGenerator = table.getGenerator().getPrimaryKeyGenerator();
        mJoinConstructor.param(primaryKeyGenerator.getJClass(), primaryKeyGenerator.getPreferredParameterName());
    }
}
