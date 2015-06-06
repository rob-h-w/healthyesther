package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

public class IdData extends DataAbstraction {
    private final DataAbstraction mParent;
    private final String mIdName;
    private Long m_id;

    public IdData(DataAbstraction parent, String name) {
        mParent = parent;
        mIdName = name;
    }

    public Long get_id() {
        return m_id;
    }

    public void set_id(Long _id) {
        if ((m_id == null && _id == null) || (m_id != null && m_id.equals(_id))) {
            return;
        }

        m_id = (_id == null || _id <= 0L) ? null : _id;
        mParent.setInDb(true);
    }

    @Override
    protected void asBundle(Bundle bundle) {
        if (m_id != null) {
            bundle.putLong(mIdName, m_id);
        }
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(mIdName, m_id);
        return values;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int idIndex = cursor.getColumnIndex(mIdName);

        m_id = cursor.getLong(idIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        if (bundle.containsKey(mIdName)) {
            m_id = bundle.getLong(mIdName);
        } else {
            m_id = null;
        }
    }
}
