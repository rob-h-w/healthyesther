package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.HealthScoreEventData;

public class HealthScoreEvent extends Table {

    public static final long EVENT_TYPE_ID = Event.Type.HEALTH_SCORE.id();
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

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private final HealthScoreEventData mValue;
        private final HealthScore.Modification mScore;
        private final Event.Modification mEvent;

        public Modification(
                HealthScore.Modification score,
                Event.Modification event,
                HealthScoreEventData value) {
            Utils.nonnull(score, event);
            mScore = score;
            mEvent = event;
            mValue = value;
            mEvent.setTypeId(EVENT_TYPE_ID);
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
            final String where = HEALTH_SCORE_ID + " = " + mScore.getRowId() +
                    " AND " +
                    EVENT_ID + " = " + mEvent.getRowId();
            Contract.getInstance().HEALTH_SCORE_EVENT.update(db, mValue.asContentValues(), where, 1, 1);
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().HEALTH_SCORE_EVENT.insert(
                    db,
                    mScore.getRowId(),
                    mEvent.getRowId(),
                    mValue.getValue());
        }
    }
}
