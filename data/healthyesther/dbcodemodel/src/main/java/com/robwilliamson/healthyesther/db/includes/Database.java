package com.robwilliamson.healthyesther.db.includes;

public abstract class Database {
    public static void create(Transaction transaction, Table[] tables) {
        for (Table table : tables) {
            table.create(transaction);
        }
    }

    public abstract Cursor select(Where where);
}
