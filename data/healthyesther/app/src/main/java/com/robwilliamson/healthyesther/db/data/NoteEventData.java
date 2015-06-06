package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.definition.NoteEvent;
import com.robwilliamson.healthyesther.db.definition.RowIdProvider;
import com.robwilliamson.healthyesther.db.definition.SimpleRowIdProvider;

public class NoteEventData extends DataAbstraction {
    private RowIdProvider mNoteIdProvider;
    private RowIdProvider mEventIdProvider;

    @Override
    protected void asBundle(Bundle bundle) {
        bundle.putLong(NoteEvent.NOTE_ID, mNoteIdProvider.getRowId());
        bundle.putLong(NoteEvent.EVENT_ID, mEventIdProvider.getRowId());
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(NoteEvent.NOTE_ID, mNoteIdProvider.getRowId());
        values.put(NoteEvent.EVENT_ID, mEventIdProvider.getRowId());
        return values;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int noteIdIndex = cursor.getColumnIndex(NoteEvent.NOTE_ID);
        final int eventIdIndex = cursor.getColumnIndex(NoteEvent.EVENT_ID);

        mNoteIdProvider = new SimpleRowIdProvider(cursor.getLong(noteIdIndex));
        mEventIdProvider = new SimpleRowIdProvider(cursor.getLong(eventIdIndex));
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        mNoteIdProvider = new SimpleRowIdProvider(bundle.getLong(NoteEvent.NOTE_ID));
        mEventIdProvider = new SimpleRowIdProvider(bundle.getLong(NoteEvent.EVENT_ID));
    }

    public RowIdProvider getEventIdProvider() {
        return mEventIdProvider;
    }

    public void setEventIdProvider(RowIdProvider eventIdProvider) {
        this.mEventIdProvider = eventIdProvider;
    }

    public RowIdProvider getNoteIdProvider() {
        return mNoteIdProvider;
    }

    public void setNoteIdProvider(RowIdProvider noteIdProvider) {
        this.mNoteIdProvider = noteIdProvider;
    }

}
