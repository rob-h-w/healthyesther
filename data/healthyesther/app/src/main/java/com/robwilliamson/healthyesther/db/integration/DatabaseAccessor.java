package com.robwilliamson.healthyesther.db.integration;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.WhereContains;

public class DatabaseAccessor extends HealthDatabase {
    private final static String HAPPINESS_NAME = "Happiness";
    private final static String DROWSINESS_NAME = "Drowsiness";
    private final static long SECOND_MS = 1000;
    private final static long MINUTE_MS = 60 * SECOND_MS;
    private final static long HOUR_MS = 60 * MINUTE_MS;
    private final static long DAY_MS = 24 * HOUR_MS;
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

        new HealthScoreTable.Row(5, HAPPINESS_NAME, true, "Happy", "Sad").applyTo(transaction);
        new HealthScoreTable.Row(5, "Energy", true, "Energetic", "Tired").applyTo(transaction);
        new HealthScoreTable.Row(1, DROWSINESS_NAME, true, "Sleepy", "Awake").applyTo(transaction);
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

            Database db = transaction.getDatabase();

            HealthScoreTable.Row[] scores = HealthDatabase.HEALTH_SCORE_TABLE.select(db, WhereContains.any());

            for (HealthScoreTable.Row score : scores) {
                if (score.getName().equals(DROWSINESS_NAME)) {
                    HealthScoreJudgmentRangeTable.Row day = new HealthScoreJudgmentRangeTable.Row(
                            score,
                            score.getBestValue(),
                            8 * HOUR_MS,
                            20 * HOUR_MS);
                    HealthScoreJudgmentRangeTable.Row night = new HealthScoreJudgmentRangeTable.Row(
                            score,
                            5L,
                            22 * HOUR_MS,
                            DAY_MS + 6 * HOUR_MS);
                    day.applyTo(transaction);
                    night.applyTo(transaction);
                } else {
                    HealthScoreJudgmentRangeTable.Row judgment = new HealthScoreJudgmentRangeTable.Row(score, score.getBestValue(), null, null);
                    judgment.applyTo(transaction);
                }
            }
        }
    }
}
