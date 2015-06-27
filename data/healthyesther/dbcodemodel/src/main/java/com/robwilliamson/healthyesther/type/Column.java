package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.Table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Column {
    public static class Comparator implements java.util.Comparator<Column> {
        @Override
        public int compare(Column c1, Column c2) {
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
                    if (constraint.getType().equals("FOREIGN KEY")) {
                        Matcher matcher = referencePattern.matcher(constraint.getDefinition());

                        if (!matcher.find()) {
                            continue;
                        }

                        String tableName = matcher.group(1);
                        String columnName = matcher.group(2);

                        mColumnDependency = new ColumnDependency(this, tableName, columnName);
                    } else if (constraint.getType().equals("NOT NULL")) {
                        mNotNull = true;
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
        return mNotNull;
    }

    public String getType() {
        return type;
    }

    public boolean isForeignKey() {
        return getColumnDependency() != null;
    }
}
