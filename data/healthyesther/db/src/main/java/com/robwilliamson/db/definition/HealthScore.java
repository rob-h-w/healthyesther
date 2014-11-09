package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.use.GetHealthScoresQuery;

public class HealthScore extends Table {
    public static class Modification extends com.robwilliamson.db.definition.Modification {

        private GetHealthScoresQuery.Score mScore;

        public Modification(long rowId) {
            setRowId(rowId);
        }

        public Modification(GetHealthScoresQuery.Score score) {
            mScore = score;

            if (score._id != null) {
                setRowId(score._id);
            }
        }

        public Modification(String name,
                            int bestValue,
                            boolean randomQuery,
                            String minLabel,
                            String maxLabel) {
            mScore = new GetHealthScoresQuery.Score(name,
                    bestValue,
                    randomQuery,
                    minLabel,
                    maxLabel);
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().HEALTH_SCORE.insert(
                        db, mScore));
            } else {
                Contract.getInstance().HEALTH_SCORE.update(db, mScore);
            }
        }
    }
    public static final int MAX = 5;
    public static final int MID = 3;
    public static final int MIN = 1;

    public static final String TABLE_NAME = "health_score";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String BEST_VALUE = "best_value";
    public static final String RANDOM_QUERY = "random_query";
    public static final String MIN_LABEL = "min_label";
    public static final String MAX_LABEL = "max_label";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE health_score ( \n" +
                "    _id          INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    name         TEXT( 140 )  NOT NULL\n" +
                "                              UNIQUE,\n" +
                "    best_value   INTEGER      NOT NULL,\n" +
                "    random_query BOOLEAN      NOT NULL\n" +
                "                              DEFAULT ( 0 ),\n" +
                "    min_label    TEXT( 140 ),\n" +
                "    max_label    TEXT( 140 ) \n" +
                ");");

        insert(db, "Happiness", MAX, true, "Sad", "Happy");
        insert(db, "Energy", MAX, true, "Tired", "Energetic");
        insert(db, "Drowsiness", MAX, true, "Sleepy", "Awake");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from == 1) {
            create(db);
        }
    }

    public long insert(
            SQLiteDatabase db,
            String name,
            int bestValue,
            boolean randomQuery,
            String minLabel,
            String maxLabel) {
        return insert(db, new GetHealthScoresQuery.Score(name, bestValue, randomQuery, minLabel, maxLabel));
    }

    public long insert(
            SQLiteDatabase db,
            GetHealthScoresQuery.Score score) {
        return insert(db, score.getContentValues());
    }

    public void update(SQLiteDatabase db,
                       GetHealthScoresQuery.Score score) {

        update(db, score.getContentValues(), score._id);
    }
}
