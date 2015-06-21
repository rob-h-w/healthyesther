package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public abstract class Row extends BaseClassGenerator {
    protected static String name(Column column) {
        return Strings.underscoresToCamel(column.getTable().getName()) + Strings.capitalize(Strings.underscoresToCamel(column.getName()));
    }

    private final Table mTable;

    public Row(Table table) throws JClassAlreadyExistsException {
        mTable = table;

        setJClass(mTable.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
    }

    public abstract void init();

    public Table getTableGenerator() {
        return mTable;
    }

    protected JType primitiveType(Column column) {
        String typeName = column.getType();
        switch (typeName) {
            case "BOOLEAN":
                return mTable.model().BOOLEAN;
            case "INTEGER":
                return mTable.model().LONG;
            case "DATETIME":
            case "TEXT":
                return mTable.model().ref(String.class);
            case "REAL":
                return mTable.model().DOUBLE;
            default:
                throw new IllegalArgumentException("Type " + typeName + " is unsupported.");
        }
    }

    protected JType nullableType(Column column) {
        String typeName = column.getType();
        switch (typeName) {
            case "BOOLEAN":
                return mTable.model().ref(Boolean.class);
            case "INTEGER":
                return mTable.model().ref(Long.class);
            case "DATETIME":
            case "TEXT":
                return mTable.model().ref(String.class);
            case "REAL":
                return mTable.model().ref(Double.class);
            default:
                throw new IllegalArgumentException("Type " + typeName + " is unsupported.");
        }
    }
}
