package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.Table;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Column {
    public static class Comparator implements java.util.Comparator<Column> {
        @Override
        public int compare(Column c1, Column c2) {
            if (!c1.isPrimaryKey() && c2.isPrimaryKey()) {
                return -1;
            }

            if (c1.isPrimaryKey() && !c2.isPrimaryKey()) {
                return 1;
            }

            if (!c1.isNotNull() && c2.isNotNull()) {
                return -1;
            }

            if (c1.isNotNull() && !c2.isNotNull()) {
                return 1;
            }

            if (!c1.isForeignKey() && c2.isForeignKey()) {
                return -1;
            }

            if (c1.isForeignKey() && !c2.isForeignKey()) {
                return 1;
            }

            return c1.getName().compareTo(c2.getName());
        }
    }

    private String name;
    private String type;
    private Constraint[] constraints;

    private transient ColumnDependency mColumnDependency;
    private transient boolean mNotNull = false;
    private transient Table mTable;
    private transient boolean mPrimaryKey = false;

    Column() {}

    public String getName() {
        return name;
    }

    public ColumnDependency getColumnDependency() {
        if (mColumnDependency == null) {

            if (constraints != null) {
                // REFERENCES event_type ( _id )
                Pattern referencePattern = Pattern.compile("REFERENCES (\\w+) \\( ([\\w\\s]+) \\).*");

                for (Constraint constraint : constraints) {
                    switch (constraint.getType()) {
                        case "PRIMARY KEY":
                            mPrimaryKey = true;
                            break;
                        case "FOREIGN KEY":
                            Matcher matcher = referencePattern.matcher(constraint.getDefinition());

                            if (!matcher.find()) {
                                continue;
                            }

                            String tableName = matcher.group(1);
                            String columnName = matcher.group(2);

                            mColumnDependency = new ColumnDependency(this, tableName, columnName);
                            break;
                        case "NOT NULL":
                            mNotNull = true;
                            break;
                    }
                }
            }
        }

        return mColumnDependency;
    }

    public void setTable(Table table) {
        mTable = table;
    }

    public Table getTable() {
        return mTable;
    }

    public boolean isNotNull() {
        getColumnDependency();
        return mNotNull;
    }

    public String getType() {
        return type;
    }

    /**
     * Get the codemodel type of the column.
     * @param model The model to use to generate basic types.
     * @return The primitive type that represents the SQLite type of this column.
     */
    public JType getJtype(JCodeModel model) {
        if (isForeignKey()) {
            return model.LONG;
        }

        switch(getType()) {
            case "TEXT":
                return model._ref(String.class);
            case "INTEGER":
                return model.LONG;
            case "REAL":
                return model.DOUBLE;
            default:
                throw new IllegalArgumentException("Unrecognized type " + getType());
        }
    }

    /**
     * Like {@link #getJtype(JCodeModel model)}, but resolves dependencies.
     * @param model The model to use to generate basic types.
     * @return The type that should be written in places that use this column.
     */
    public JType getDependentJtype(JCodeModel model) {
        JType type = getJtype(model);
        if (isForeignKey()) {
            type = getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
        }

        return type;
    }

    public boolean isForeignKey() {
        return getColumnDependency() != null;
    }

    public boolean isPrimaryKey() {
        getColumnDependency();
        return mPrimaryKey || getTable().isPrimaryKey(this);
    }
}
