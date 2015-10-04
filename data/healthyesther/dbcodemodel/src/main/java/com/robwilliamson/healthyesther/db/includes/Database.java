package com.robwilliamson.healthyesther.db.includes;

public abstract class Database {
    public static void create(Transaction transaction, Table[] tables) {
        for (Table table : tables) {
            table.create(transaction);
        }
    }

    public static void drop(Transaction transaction, Table[] tables) {
        for (Table table : tables) {
            table.drop(transaction);
        }
    }

    public static void upgrade(Transaction transaction, int from, int to, Table[] tables) {
        for (Table table : tables) {
            table.upgrade(transaction, from, to);
        }
    }

    public abstract Cursor select(Where where);
}
