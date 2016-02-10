package com.robwilliamson.healthyesther.db.integration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;

import javax.annotation.Nonnull;

public class V4 implements Upgrade {
    static void createDefaultJudgmentFor(
            @Nonnull HealthScoreTable.Row score,
            long best,
            @Nonnull com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        score.applyTo(transaction);
        switch (score.getName()) {
            case DatabaseAccessor.DROWSINESS_NAME:
                createDayTimeJudgmentFor(score, best, transaction);
                createNightTimeJudgmentFor(score, 5, transaction);
                break;
            case DatabaseAccessor.ENERGY_NAME:
                createDayTimeJudgmentFor(score, best, transaction);
                createNightTimeJudgmentFor(score, 2, transaction);
                break;
            default:
                HealthScoreJudgmentRangeTable.Row judgment = new HealthScoreJudgmentRangeTable.Row(score, best, null, null);
                judgment.applyTo(transaction);
                break;
        }
    }

    private static void createNightTimeJudgmentFor(
            @Nonnull HealthScoreTable.Row score,
            long best,
            @Nonnull com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        new HealthScoreJudgmentRangeTable.Row(
                score,
                best,
                22 * Utils.Time.HOUR_MS,
                Utils.Time.DAY_MS + 6 * Utils.Time.HOUR_MS).applyTo(transaction);
    }

    private static void createDayTimeJudgmentFor(
            @Nonnull HealthScoreTable.Row score,
            long best,
            @Nonnull com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        new HealthScoreJudgmentRangeTable.Row(
                score,
                best,
                8 * Utils.Time.HOUR_MS,
                20 * Utils.Time.HOUR_MS).applyTo(transaction);
    }

    @Override
    public void upgradeFrom(@Nonnull Transaction transaction) {
        SQLiteDatabase sqLiteDb = transaction.db();

        DatabaseAccessor.deactivateForeignKeyChecking(sqLiteDb);

        final String name = HealthDatabase.HEALTH_SCORE_TABLE.getName();
        final String oldName = "old_" + name;

        sqLiteDb.execSQL("ALTER TABLE " + name + " RENAME TO " + oldName + ";");

        try (Cursor oldScores = sqLiteDb.query(
                oldName,
                null,
                null,
                null,
                null,
                null,
                "_id ASC")) {

            HealthDatabase.create(transaction);

            if (oldScores.moveToFirst()) {
                final int NAME = oldScores.getColumnIndex("name");
                final int BEST = oldScores.getColumnIndex("best_value");
                final int RANDOM = oldScores.getColumnIndex("random_query");
                final int MIN = oldScores.getColumnIndex("min_label");
                final int MAX = oldScores.getColumnIndex("max_label");

                do {
                    HealthScoreTable.Row newScore = new HealthScoreTable.Row(
                            oldScores.getString(NAME),
                            oldScores.getLong(RANDOM) != 0L,
                            oldScores.getString(MAX),
                            oldScores.getString(MIN)
                    );

                    newScore.applyTo(transaction);

                    createDefaultJudgmentFor(newScore, oldScores.getLong(BEST), transaction);
                } while (oldScores.moveToNext());
            }

            sqLiteDb.execSQL("DROP TABLE IF EXISTS " + oldName + ";");

            DatabaseAccessor.checkForeignKeys(sqLiteDb);
            DatabaseAccessor.activateForeignKeyChecking(sqLiteDb);
        }
    }
}
