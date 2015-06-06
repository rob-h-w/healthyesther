package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Note;

public class NoteData extends DataAbstraction {
    private final static String _ID = NoteData.class.getCanonicalName() + "_ID";
    private final static String NAME = NoteData.class.getCanonicalName() + "NAME";
    private final static String NOTE = NoteData.class.getCanonicalName() + "NOTE";

    private Long m_id;
    private String mName;
    private String mNote;

    @Override
    protected void asBundle(Bundle bundle) {
        if (m_id != null) {
            bundle.putLong(_ID, m_id);
        }

        bundle.putString(NAME, mName);
        bundle.putString(NOTE, mNote);
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(Note.NAME, mName);
        values.put(Note.NOTE, mNote);
        return values;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int nameIndex = cursor.getColumnIndex(Note.NAME);
        final int noteIndex = cursor.getColumnIndex(Note.NOTE);

        mName = cursor.getString(nameIndex);
        mNote = cursor.getString(noteIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        mName = bundle.getString(NAME);
        mNote = bundle.getString(NOTE);
    }

    public Long get_id() {
        return m_id;
    }

    public void set_id(Long _id) {
        this.m_id = _id;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        if (Utils.Strings.equals(mNote, note)) {
            return;
        }

        setModified(true);
        this.mNote = note;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        if (Utils.Strings.equals(mName, name)) {
            return;
        }

        setModified(true);
        this.mName = name;
    }
}
