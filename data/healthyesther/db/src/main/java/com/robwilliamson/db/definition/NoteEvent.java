package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;

public class NoteEvent extends Table {
    public static class Modification extends com.robwilliamson.db.definition.Modification {

        private final long mNoteId;
        private final long mEventId;

        public Modification(long noteId, long eventId) {
            mNoteId = noteId;
            mEventId = eventId;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().NOTE_EVENT.insert(db, mNoteId, mEventId));
            }
        }
    }

    public static final long EVENT_TYPE_ID = 5;
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
}
