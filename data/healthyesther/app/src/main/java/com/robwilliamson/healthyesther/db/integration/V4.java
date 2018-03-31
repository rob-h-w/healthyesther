/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Where;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Nonnull
    private static Object addTimezone(@Nullable String time, @Nonnull ZoneId zone) {
        if (time == null) {
            return DateTime.class;
        }

        ZonedDateTime dateTime;

        if (time.contains("T")) {
            if (time.contains(" -") || time.contains(" +")) {
                dateTime = Utils.Time.fromDatabaseString(time);
            } else {
                dateTime = Utils.Time.fromUtcString(time);
            }
        } else {
            time = time.replace(' ', 'T');
            dateTime = Utils.Time.fromUtcString(time).withZoneSameLocal(zone);
        }

        return DateTime.from(dateTime);
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

        try (final Cursor events = sqLiteDb.query(
                HealthDatabase.EVENT_TABLE.getName(),
                null,
                null,
                null,
                null,
                null,
                "_id ASC")) {

            if (events.moveToFirst()) {
                final int _ID = events.getColumnIndex("_id");
                final int WHEN = events.getColumnIndex("when");
                final int CREATED = events.getColumnIndex("created");
                final int MODIFIED = events.getColumnIndex("modified");

                do {
                    transaction.update(
                            HealthDatabase.EVENT_TABLE.getName(),
                            new Where() {
                                @Nullable
                                @Override
                                public String getWhere() {
                                    return "_id = " + events.getInt(_ID);
                                }
                            },
                            Arrays.asList("[when]", "created", "modified"),
                            addTimezone(events.getString(WHEN), ZoneId.systemDefault()),
                            addTimezone(events.getString(CREATED), ZoneOffset.UTC),
                            addTimezone(events.getString(MODIFIED), ZoneOffset.UTC));
                } while (events.moveToNext());
            }
        }
    }
}
