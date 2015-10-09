package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public abstract class Database {
    public static void create(@Nonnull Transaction transaction, @Nonnull Table[] tables) {
        for (Table table : tables) {
            table.create(transaction);
        }
    }

    public static void drop(@Nonnull Transaction transaction, @Nonnull Table[] tables) {
        for (Table table : tables) {
            table.drop(transaction);
        }
    }

    public static void upgrade(@Nonnull Transaction transaction, int from, int to, @Nonnull Table[] tables) {
        for (Table table : tables) {
            table.upgrade(transaction, from, to);
        }
    }

    @Nonnull
    public abstract Cursor select(@Nonnull Where where, @Nonnull Table table);
}
