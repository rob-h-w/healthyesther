package com.robwilliamson.db.column.constraint;

import com.robwilliamson.db.column.ColumnDefinition;
import com.robwilliamson.db.column.constraint.Constraint;

/**
 * Accessor for constraint objects.
 */
public final class Constraints {
    private Constraints() {}

    public static final PrimaryKey primaryKey() {
        return new PrimaryKey();
    }

    public static final Constraint notNull() {
        return new NotNull();
    }

    public static final References references(final String foreignTable,
                                              final ColumnDefinition foreignColumn,
                                              final References.Reaction... reactions) {
        return new References(foreignTable,
                foreignColumn,
                reactions);
    }

    public static final Constraint unique() {
        return new Unique();
    }
}
