package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;

public class HealthScoreEvent extends Table {

    public static class Modification extends com.robwilliamson.db.definition.Modification {
        private final HealthScore.Modification mScore;
        private final Event.Modification mEvent;
        private final int mValue;

        public Modification(
                HealthScore.Modification score,
                Event.Modification event,
                int value) {
            mScore = score;
            mEvent = event;
            mValue = value;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            mScore.modify(db);
            setRowId(
                    Contract.getInstance().HEALTH_SCORE_EVENT.insert(
                            db,
                            mScore.getRowId(),
                            mEvent.getRowId(),
                            mValue));
        }
    }

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

    public long insert(SQLiteDatabase db, long healthScoreId, long eventId, int score) {
        ContentValues values = new ContentValues();
        values.put(HEALTH_SCORE_ID, healthScoreId);
        values.put(EVENT_ID, eventId);
        values.put(SCORE, score);
        return insert(db, values);
    }
}
