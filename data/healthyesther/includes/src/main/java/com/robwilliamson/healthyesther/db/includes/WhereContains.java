package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WhereContains {
    public static Where all() {
        return new Where() {
            @Nullable
            @Override
            public String getWhere() {
                return null;
            }
        };
    }

    public static Where and(final @Nonnull Where... wheres) {
        return new Where() {
            @Nullable
            @Override
            public String getWhere() {
                String separator = "";
                StringBuilder string = new StringBuilder();
                for (Where where : wheres) {
                    string.append(separator).append("(").append(where.getWhere()).append(")");
                    separator = " AND ";
                }
                return string.toString();
            }
        };
    }

    public static Where foreignKey(@Nonnull final String columnName, @Nullable final Object value) {
        return new Where() {
            @Nullable
            @Override
            public String getWhere() {
                return columnName + " = " + value;
            }
        };
    }

    public static Where columnEqualling(@Nonnull final String columnName, @Nullable final String value) {
        return new Where() {
            @Nullable
            @Override
            public String getWhere() {
                String strValue = value == null ? "null" : value;
                return columnName + " = " + strValue;
            }
        };
    }
}
