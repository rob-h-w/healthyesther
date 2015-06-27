package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public abstract class RowGenerator extends BaseClassGenerator {
    protected static String name(Column column) {
        return Strings.underscoresToCamel(column.getTable().getName()) + Strings.capitalize(Strings.underscoresToCamel(column.getName()));
    }

    protected static String name(RowGenerator rowGenerator) {
        return Strings.lowerCase(rowGenerator.getTableGenerator().getPreferredParameterName()) + "Row";
    }

    private final TableGenerator mTableGenerator;

    public RowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        mTableGenerator = tableGenerator;

        setJClass(mTableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
    }

    public abstract void init();

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
}
