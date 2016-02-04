package com.robwilliamson.healthyesther.db.integration;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Table;

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

        if (from == 3) {
            Upgrade.from3(transaction);
        }

        if (from == 4) {
            Upgrade.from4(transaction);
        }
    }

    private static class Upgrade {
        public static void from3(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
            SQLiteDatabase db = ((Transaction) transaction).db();
            try {
                db.beginTransaction();
                try (
                        android.database.Cursor cursor = db.query(
                                "health_score",
                                new String[]{"_id"},
                                "name == ?",
                                new String[]{"Drowsiness"},
                                null,
                                null,
                                null)) {
                    final int idID = cursor.getColumnIndex("_id");
                    cursor.moveToFirst();
                    long id = cursor.getLong(idID);
                    ContentValues value = new ContentValues();
                    value.put("min_label", "Awake");
                    value.put("max_label", "Sleepy");
                    db.update(
                            "health_score",
                            value,
                            "_id == ?",
                            new String[]{String.valueOf(id)});
                    db.setTransactionSuccessful();
                }
            } finally {
                db.endTransaction();
            }
        }

        public static void from4(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
            HealthDatabase.create(transaction);
        }
    }
}
