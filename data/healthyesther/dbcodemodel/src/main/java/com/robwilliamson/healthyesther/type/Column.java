package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.Table;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Column {
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String type;
    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    private Constraint[] constraints;
    private transient ColumnDependency mColumnDependency;
    private transient boolean mNotNull = false;
    private transient Table mTable;
    private transient boolean mPrimaryKey = false;
    private transient Integer mMaxLength = null;

    Column() {
    }

    @Nullable
    public Integer getMaxLength() {
        if (!isString()) {
            return null;
        }

        if (mMaxLength == null) {
            String ddl = mTable.getDdl();
            Pattern re = Pattern.compile(".*" + getName() + "\\s+TEXT\\s*\\(\\s*(\\d+)\\s*\\).*", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher matcher = re.matcher(ddl);
            if (matcher.find()) {
                mMaxLength = Integer.valueOf(matcher.group(1));
            }
        }

        return mMaxLength;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public String getVarName() {
        return Strings.lowerCase(Strings.underscoresToCamel(getName()));
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

    public Table getTable() {
        return mTable;
    }

    public void setTable(Table table) {
        mTable = table;
    }

    public boolean isNotNull() {
        getColumnDependency();
        return mNotNull;
    }

    @Nonnull
    public String getType() {
        return type;
    }

    /**
     * Get the codemodel type of the column.
     *
     * @param model The model to use to generate basic types.
     * @return The primitive type that represents the SQLite type of this column.
     */
    @Nonnull
    public JType getPrimitiveType(@Nonnull JCodeModel model) {
        if (isForeignKey() || mPrimaryKey) {
            return model.LONG;
        }

        switch (getType()) {
            case "BOOLEAN":
                return model.BOOLEAN;
            case "DATETIME":
                return model._ref(DateTime.class);
            case "INTEGER":
                return model.LONG;
            case "REAL":
                return model.DOUBLE;
            case "TEXT":
                return model._ref(String.class);
            default:
                throw new IllegalArgumentException("Unrecognized type " + getType());
        }
    }

    public boolean isString() {
        return getType().equals("TEXT");
    }

    @Nonnull
    public JType getNullableType(@Nonnull JCodeModel model) {
        String typeName = getType();
        switch (typeName) {
            case "BOOLEAN":
                return model.ref(Boolean.class);
            case "DATETIME":
                return model._ref(DateTime.class);
            case "INTEGER":
                return model.ref(Long.class);
            case "REAL":
                return model.ref(Double.class);
            case "TEXT":
                return model._ref(String.class);
            default:
                return handleUnknownColumnType(model.ref(Long.class));
        }
    }

    @Nonnull
    private JType handleUnknownColumnType(@Nonnull JType indexer) {
        String typeName = getType();

        if (Strings.isEmpty(typeName)) {
            if (isForeignKey()) {
                return indexer;
            }
        }

        throw new IllegalArgumentException("Type " + typeName + " is unsupported.");
    }

    /**
     * Like {@link #getPrimitiveType(JCodeModel)}, but resolves dependencies.
     *
     * @param model The model to use to generate basic types.
     * @return The type that should be written in places that use this column.
     */
    @Nonnull
    public JType getDependentJtype(@Nonnull JCodeModel model) {
        @Nonnull
        JType type;
        if (isNotNull()) {
            type = getPrimitiveType(model);
        } else {
            type = getNullableType(model);
        }

        if (isForeignKey()) {
            type = getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
        } else if (mPrimaryKey) {
            type = getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
        }

        return type;
    }

    public boolean isRowId() {
        return getName().equals("_id");
    }

    public boolean isForeignKey() {
        return getColumnDependency() != null;
    }

    public boolean isPrimaryKey() {
        getColumnDependency();
        return mPrimaryKey || getTable().isPrimaryKey(this);
    }

    public interface Picker {
        boolean pick(Column column);
    }

    public interface Visitor<T> {
        void visit(Column column, T context);
    }

    public static class AllPicker implements Picker {

        @Override
        public boolean pick(Column column) {
            return true;
        }
    }

    public static class BasicPicker implements Picker {

        @Override
        public boolean pick(Column column) {
            return !column.isPrimaryKey();
        }
    }

    public static class PrimaryPicker implements Picker {
        @Override
        public boolean pick(Column column) {
            return column.isPrimaryKey();
        }
    }

    public static class Comparator implements java.util.Comparator<Column> {
        @Override
        public int compare(Column c1, Column c2) {
            if (!c1.isPrimaryKey() && c2.isPrimaryKey()) {
                return 1;
            }

            if (c1.isPrimaryKey() && !c2.isPrimaryKey()) {
                return -1;
            }

            if (!c1.isNotNull() && c2.isNotNull()) {
                return 1;
            }

            if (c1.isNotNull() && !c2.isNotNull()) {
                return -1;
            }

            if (!c1.isForeignKey() && c2.isForeignKey()) {
                return 1;
            }

            if (c1.isForeignKey() && !c2.isForeignKey()) {
                return -1;
            }

            return c1.getName().compareTo(c2.getName());
        }
    }
}
