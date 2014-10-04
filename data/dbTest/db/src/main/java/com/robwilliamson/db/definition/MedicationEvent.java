package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class MedicationEvent extends Table {
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

    public void insert(SQLiteDatabase db, long medicationId, long eventId) {
        ContentValues values = new ContentValues();
        values.put(MEDICATION_ID, medicationId);
        values.put(EVENT_ID, eventId);
        insert(db, values);
    }
}
