package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.definition.HealthScoreEvent;

public class HealthScoreEventData extends DataAbstraction {
    private final static String VALUE = HealthScoreEventData.class.getCanonicalName() + "value";

    private int mValue;

    @Override
    protected void asBundle(Bundle bundle) {
        bundle.putInt(VALUE, mValue);
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(VALUE, mValue);
        return values;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int valueIndex = cursor.getColumnIndex(HealthScoreEvent.SCORE);
        mValue = cursor.getInt(valueIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        mValue = bundle.getInt(VALUE);
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        if (mValue == value) {
            return;
        }

        setModified(true);
        this.mValue = value;
    }
}
