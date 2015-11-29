package com.robwilliamson.healthyesther.db.integration;

import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.*;

public class DatabaseAccessor extends HealthDatabase {
    private final static Table[] TABLES;

    static {
        TABLES = new Table[HealthDatabase.TABLES.length - 1];
        for (int i = 0, k = 0; i < TABLES.length; i++, k++) {
            if (HealthDatabase.TABLES[k] == HealthDatabase.ANDROID_METADATA_TABLE) {
                k++;
            }

            TABLES[i] = HealthDatabase.TABLES[k];
        }
    }

    public static void create(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        Database.create(transaction, TABLES);
        EventTypeTable.populateTable(transaction);

        new HealthScoreTable.Row(5, "Happiness", true, "Happy", "Sad").applyTo(transaction);
        new HealthScoreTable.Row(5, "Energy", true, "Energetic", "Tired").applyTo(transaction);
        new HealthScoreTable.Row(1, "Drowsiness", true, "Sleepy", "Awake").applyTo(transaction);
    }

    public static void drop(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        Database.drop(transaction, TABLES);
    }

    public static void upgrade(com.robwilliamson.healthyesther.db.includes.Transaction transaction, int from, int to) {
        Database.upgrade(transaction, from, to, TABLES);
    }
}
