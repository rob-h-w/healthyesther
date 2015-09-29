package com.robwilliamson.healthyesther.db.includes;

public abstract class Database {
    public final void create(Transaction transaction) {
        Table[] tables = getTables();

        for (Table table : tables) {
            table.create(transaction);
        }
    }

    public abstract Table[] getTables();
}
