package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.Column;
import com.robwilliamson.healthyesther.type.DbObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Table extends DbObject {
    public static class DependencyLoopException extends RuntimeException {
        private final String mMessage;

        DependencyLoopException(List<Table> unsatisifiedDependencies) {
            StringBuilder builder = new StringBuilder();
            builder.append("One or more tables had dependencies that could not be satisfied. ")
                    .append("These were:\n")
                    .append("\n");

            for (Table table : unsatisifiedDependencies) {
                builder.append(table.getName()).append("\n");
            }

            builder.append("\n")
                    .append("These have dependencies:\n");

            String separator = "";
            for (Table table: unsatisifiedDependencies) {
                builder.append(separator);
                builder.append(table.getName()).append(":{\n");
                final String columnIndent = "    ";
                final String dependencyIndent = "        ";

                for (Column column : table.getColumns()) {
                    if (column.getColumnDependencies().isEmpty()) {
                        continue;
                    }

                    builder.append(columnIndent).append(column.getName()).append(":{\n");

                    for (ColumnDependency dependency : column.getColumnDependencies()) {
                        builder.append(dependencyIndent)
                                .append(dependency.table)
                                .append(": ")
                                .append(dependency.column);
                    }

                    builder.append(columnIndent).append("}\n");
                }

                builder.append("}\n");

                separator = "\n";
            }

            mMessage = builder.toString();
        }

        /**
         * Returns the detail message string of this throwable.
         *
         * @return the detail message string of this {@code Throwable} instance
         * (which may be {@code null}).
         */
        @Override
        public String getMessage() {
            return mMessage;
        }
    }

    public static List<Table> getTables(DbObject[] objects) {
        List<Table> tables = new ArrayList<>(objects.length);

        for (DbObject object : objects) {
            if (object.isTable()) {
                Table table = new Table(object);
                table.init();
                tables.add(table);
            }
        }

        for (Table table : tables) {
            for (Column column : table.getColumns()) {
                for (ColumnDependency dependency : column.getColumnDependencies()) {
                    Column columnDependendOn = byDependency(tables, dependency);
                    dependency.setDependency(columnDependendOn);
                }
            }
        }
        
        // Sort by dependencies. Zero dependencies first.
        List<Table> sortedTables = new ArrayList<>(tables.size());
        int remainingTries = tables.size();
        while(remainingTries > 0) {
            for (Table table : tables) {
                if (!table.hasDependencies() ||
                        sortedTables.containsAll(table.getTablesDependedOn())) {
                    sortedTables.add(table);
                }
            }

            for (Table table : sortedTables) {
                tables.remove(table);
            }

            if (tables.isEmpty()) {
                break;
            }

            remainingTries--;
        }

        if (!tables.isEmpty()) {
            throw new DependencyLoopException(tables);
        }

        return sortedTables;
    }

    public static Column byDependency(List<Table> tables, ColumnDependency dependency) {
        Table table = byName(tables, dependency.table);
        if (table == null) {
            return null;
        }

        for (Column column : table.getColumns()) {
            if (column.getName().equals(dependency.column)) {
                return column;
            }
        }

        return null;
    }

    public static Table byName(List<Table> tables, String name) {
        for (Table table : tables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }

        return null;
    }

    private com.robwilliamson.healthyesther.generator.Table mGenerator;

    public Table(DbObject table) {
        if (!table.isTable()) {
            throw new IllegalArgumentException("The DbObject must be a table object.");
        }

        table.copyTo(this);
    }

    public void init() {
        for (Column column : getColumns()) {
            column.setTable(this);
        }
    }

    public boolean hasDependencies() {
        for (Column column : getColumns()) {
            if (!column.getColumnDependencies().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public Set<Table> getTablesDependedOn() {
        Set<Table> tableDependencies = new HashSet<>();
        for (Column column : getColumns()) {
            for (ColumnDependency dependency : column.getColumnDependencies()) {
                tableDependencies.add(dependency.getDependency().getTable());
            }
        }

        return tableDependencies;
    }

    public void setGenerator(com.robwilliamson.healthyesther.generator.Table generator) {
        mGenerator = generator;
    }

    public com.robwilliamson.healthyesther.generator.Table getmGenerator() {
        return mGenerator;
    }
}
