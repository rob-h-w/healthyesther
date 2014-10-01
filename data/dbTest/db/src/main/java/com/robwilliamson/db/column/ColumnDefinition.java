package com.robwilliamson.db.column;

import com.robwilliamson.db.Type;
import com.robwilliamson.db.column.constraint.*;

/**
 * Defines a single column in a relational SQL database.
 */
public class ColumnDefinition {
    public final String name;
    public final Type type;
    public final Integer maximumLength;
    public final Constraint [] constraints;
    public final String defaultValue;

    public static class NameNullException extends RuntimeException {}
    public static class NameEmptyException extends RuntimeException {}
    public static class TypeNullException extends RuntimeException {}

    public ColumnDefinition (final String name, final Type type, final String dfault, final Constraint... constraints) {
        this(name, type, null, dfault, constraints);
    }

    public ColumnDefinition (final String name, final Type type, final Integer maximumLength, final String dfault, final Constraint... constraints) {
        this.name = name;
        this.type = type;
        this.maximumLength = maximumLength;
        defaultValue = dfault;
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(name).append(" ");
        str.append(type.toString(maximumLength)).append(" ");

        for (Constraint constraint : constraints) {
            str.append(constraint);
        }

        if (defaultValue != null) {
            str.append(defaultValue).append(" ");
        }

        return str.toString();
    }
}
