package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.MedicationEventData;

public class MedicationEvent extends Table {
    public static final long EVENT_TYPE_ID = Event.Type.MEDICATION.id();
    public static final String TABLE_ID = "medication_event";
    public static final String MEDICATION_ID = "medication_id";
    public static final String EVENT_ID = "event_id";

    @Override
    public String getName() {
        return TABLE_ID;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medication_event ( \n" +
                "    medication_id  NOT NULL\n" +
                "                   REFERENCES medication ( _id ) ON DELETE CASCADE\n" +
                "                                                 ON UPDATE CASCADE,\n" +
                "    event_id       NOT NULL\n" +
                "                   REFERENCES event ( _id ) ON DELETE CASCADE\n" +
                "                                            ON UPDATE CASCADE,\n" +
                "    PRIMARY KEY ( medication_id, event_id ) \n" +
                ");");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }

    public long insert(SQLiteDatabase db, long medicationId, long eventId) {
        ContentValues values = new ContentValues();
        values.put(MEDICATION_ID, medicationId);
        values.put(EVENT_ID, eventId);
        return insert(db, values);
    }

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {

        private final Event.Modification mEvent;
        private final Medication.Modification mMedication;
        private final MedicationEventData mValue;

        public Modification(Medication.Modification medication,
                            Event.Modification event) {
            mMedication = medication;
            mEvent = event;
            mValue = new MedicationEventData();
        }

        @Override
        protected void onStartModify(SQLiteDatabase db) {
            mMedication.modify(db);
            mEvent.setTypeId(EVENT_TYPE_ID);
            mEvent.modify(db);
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().MEDICATION_EVENT.insert(db, mMedication.getRowId(), mEvent.getRowId());
        }
    }
}
