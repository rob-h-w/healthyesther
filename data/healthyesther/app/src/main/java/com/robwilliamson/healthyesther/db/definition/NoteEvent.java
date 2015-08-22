package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.NoteEventData;

public class NoteEvent extends Table {
    public static final long EVENT_TYPE_ID = Event.Type.NOTE.id();
    public static final String TABLE_NAME = "note_event";
    public static final String NOTE_ID = "note_id";
    public static final String EVENT_ID = "event_id";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE note_event ( \n" +
                "    note_id   NOT NULL\n" +
                "              REFERENCES note ( _id ) ON DELETE CASCADE\n" +
                "                                      ON UPDATE CASCADE,\n" +
                "    event_id  NOT NULL\n" +
                "              REFERENCES event ( _id ) ON DELETE CASCADE\n" +
                "                                       ON UPDATE CASCADE,\n" +
                "    PRIMARY KEY ( note_id, event_id ) \n" +
                ");\n");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from == 2) {
            create(db);
        }
    }

    private long insert(SQLiteDatabase db, long mNoteId, long mEventId) {
        ContentValues values = new ContentValues();
        values.put(NOTE_ID, mNoteId);
        values.put(EVENT_ID, mEventId);
        return insert(db, values);
    }

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private final NoteEventData mValue;

        public Modification(Note.Modification note, Event.Modification event) {
            mValue = new NoteEventData();
            mValue.setEventIdProvider(event);
            mValue.setNoteIdProvider(note);
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
            final String where = EVENT_ID + " = " + mValue.getEventIdProvider().getRowId() +
                    NOTE_ID + " = " + mValue.getNoteIdProvider().getRowId();
            Contract.getInstance().NOTE_EVENT.update(db, mValue.asContentValues(), where, 1, 1);
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().NOTE_EVENT.insert(db, mValue.asContentValues());
        }
    }
}
