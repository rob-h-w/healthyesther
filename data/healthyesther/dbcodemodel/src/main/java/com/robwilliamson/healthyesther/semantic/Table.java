package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.generator.TableGenerator;
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
                    if (!column.isForeignKey()) {
                        continue;
                    }

                    builder.append(columnIndent).append(column.getName()).append(":{\n");

                    ColumnDependency dependency = column.getColumnDependency();
                    builder.append(dependencyIndent)
                            .append(dependency.table)
                            .append(": ")
                            .append(dependency.column);

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
                if (!column.isForeignKey()) {
                    continue;
                }

                ColumnDependency dependency = column.getColumnDependency();
                Column columnDependendOn = byDependency(tables, dependency);
                dependency.setDependency(columnDependendOn);
            }
        }
        
        // Sort by dependencies. Zero dependencies first.
        List<Table> sortedTables = new ArrayList<>(tables.size());
        int remainingTries = tables.size();
        while(remainingTries > 0) {
            for (Table table : tables) {
                if (!table.hasDependencies() ||
                        sortedTables.containsAll(table.getTableDependencies())) {
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

    private TableGenerator mGenerator;

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
            if (column.isForeignKey()) {
                return true;
            }
        }

        return false;
    }

    public Set<Table> getTableDependencies() {
        Set<Table> tableDependencies = new HashSet<>();
        for (Column column : getColumns()) {
            if (column.isForeignKey()) {
                tableDependencies.add(column.getColumnDependency().getDependency().getTable());
            }
        }

        return tableDependencies;
    }

    public void setGenerator(TableGenerator generator) {
        mGenerator = generator;
    }

    public TableGenerator getGenerator() {
        return mGenerator;
    }
}
