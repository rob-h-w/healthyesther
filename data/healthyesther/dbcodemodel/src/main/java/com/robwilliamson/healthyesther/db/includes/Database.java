package com.robwilliamson.healthyesther.db.includes;

public final class Database {
    public static void create(Transaction transaction, Table[] tables) {
        for (Table table : tables) {
            table.create(transaction);
        }
    }
}
