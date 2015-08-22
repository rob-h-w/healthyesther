package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Meal;

public class MealData extends DataAbstraction {
    private Long m_id;
    private String mName;
    public MealData() {
    }

    public MealData(String name) {
        this(null, name);
    }

    public MealData(Long _id, String name) {
        this.m_id = _id == null || _id == 0L ? null : _id;

        if (!validateName(name)) {
            throw new BadNameLength();
        }
        this.mName = name;
    }

    public static boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 140);
    }

    @Override
    protected void asBundle(Bundle bundle) {
        if (m_id != null) {
            bundle.putLong(Meal._ID, m_id);
        }

        bundle.putString(Meal.NAME, mName);
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();

        if (m_id != null) {
            contentValues.put(Meal._ID, m_id);
        }

        contentValues.put(Meal.NAME, mName);
        return contentValues;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int _idIndex = cursor.getColumnIndex(Meal._ID);
        final int nameIndex = cursor.getColumnIndex(Meal.NAME);

        m_id = cursor.getLong(_idIndex);
        mName = cursor.getString(nameIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        m_id = null;

        if (bundle.containsKey(Meal._ID)) {
            m_id = bundle.getLong(Meal._ID);
        }

        mName = bundle.getString(Meal.NAME);
    }

    public Long get_id() {
        return m_id;
    }

    public void set_id(Long m_id) {
        setModified(true);
        setInDb(true);
        this.m_id = m_id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        setModified(true);
        this.mName = name;
    }

    public static class BadNameLength extends IllegalArgumentException {
    }
}
