package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Column {
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
