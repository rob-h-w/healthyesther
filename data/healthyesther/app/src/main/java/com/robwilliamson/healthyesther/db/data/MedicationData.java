package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Medication;

public class MedicationData extends DataAbstraction {
    private final static String _ID = MedicationData.class.getCanonicalName() + "_ID";
    private final static String NAME = MedicationData.class.getCanonicalName() + "NAME";

    private Long m_id;
    private String mName;

    public Long get_id() {
        return m_id;
    }

    public void set_id(Long _id) {
        this.m_id = _id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        if (Utils.Strings.equals(mName, name)) {
            return;
        }

        Medication.checkName(name);

        setModified(true);
        mName = name;
    }

    @Override
    protected void asBundle(Bundle bundle) {
        if (m_id != null) {
            bundle.putLong(_ID, m_id);
        }
        bundle.putString(NAME, mName);
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(Medication.NAME, mName);
        return values;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int idIndex = cursor.getColumnIndex(Medication._ID);
        final int nameIndex = cursor.getColumnIndex(Medication.NAME);
        m_id = cursor.getLong(idIndex);
        mName = cursor.getString(nameIndex);
        setModified(false);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        if (bundle.containsKey(_ID)) {
            m_id = bundle.getLong(_ID);
        } else {
            m_id = null;
        }
        mName = bundle.getString(NAME);
    }
}
