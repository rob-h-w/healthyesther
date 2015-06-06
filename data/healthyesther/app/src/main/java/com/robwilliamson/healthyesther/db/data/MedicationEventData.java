package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

public class MedicationEventData extends DataAbstraction {
    @Override
    protected void asBundle(Bundle bundle) {}

    @Override
    public ContentValues asContentValues() {
        return new ContentValues();
    }

    @Override
    protected void populateFrom(Cursor cursor) {

    }

    @Override
    protected void populateFrom(Bundle bundle) {

    }
}
