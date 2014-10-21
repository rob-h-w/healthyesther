package com.robwilliamson.db.definition;

import android.database.sqlite.SQLiteDatabase;

public class HealthScoreEvent extends Table {

    public static final long EVENT_TYPE_ID = 3;

    public static final String TABLE_NAME = "health_score_event";
    public static final String HEALTH_SCORE_ID = "health_score_id";
    public static final String EVENT_ID = "event_id";
    public static final String SCORE = "score";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE health_score_event ( \n" +
                "    health_score_id         NOT NULL\n" +
                "                            REFERENCES health_score ( _id ) ON DELETE CASCADE\n" +
                "                                                            ON UPDATE CASCADE,\n" +
                "    event_id                NOT NULL\n" +
                "                            REFERENCES event ( _id ) ON DELETE CASCADE\n" +
                "                                                     ON UPDATE CASCADE,\n" +
                "    score           INTEGER,\n" +
                "    PRIMARY KEY ( health_score_id, event_id ) \n" +
                ");\n");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from == 1) {
            create(db);
        }
    }
}
