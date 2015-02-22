package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;

public class NoteEvent extends Table {
    public static class Modification extends com.robwilliamson.db.definition.Modification {

        private final Note.Modification mNote;
        private final Event.Modification mEvent;

        public Modification(Note.Modification note, Event.Modification event) {
            this.mNote = note;
            mEvent = event;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            mEvent.setTypeId(EVENT_TYPE_ID);
            mEvent.modify(db);
            mNote.modify(db);

            if (getRowId() == null) {
                setRowId(Contract.getInstance().NOTE_EVENT.insert(db, mNote.getRowId(), mEvent.getRowId()));
            }
        }
    }

    public static final long EVENT_TYPE_ID = 4;
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
