package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Utils;

public class HealthScore extends Table {
    public static class Modification extends com.robwilliamson.db.definition.Modification {

        @Override
        public void modify(SQLiteDatabase db) {

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
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(BEST_VALUE, bestValue);
        values.put(RANDOM_QUERY, randomQuery);

        if (minLabel != null) {
            values.put(MIN_LABEL, minLabel);
        }

        if (maxLabel != null) {
            values.put(MAX_LABEL, maxLabel);
        }

        return insert(db, values);
    }
}
