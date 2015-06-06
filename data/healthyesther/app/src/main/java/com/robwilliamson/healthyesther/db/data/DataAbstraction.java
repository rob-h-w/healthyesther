package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public abstract class DataAbstraction {
    private static String IN_DB = DataAbstraction.class.getCanonicalName() + "in database";
    private static String MODIFIED = DataAbstraction.class.getCanonicalName() + "modified";

    private boolean mInDb = false;
    private boolean mModified = false;

    public static class BadDataAbstractionException extends RuntimeException {
        public BadDataAbstractionException(Throwable causedBy) {
            super("Implementations must have accessible no-value constructors.", causedBy);
        }
    }

    public static <T extends DataAbstraction> T from(Bundle bundle, Class<T> type) {
        T value = newInstance(type);
        value.setInDb(bundle.getBoolean(IN_DB));
        value.setModified(bundle.getBoolean(MODIFIED));
        value.populateFrom(bundle);
        return value;
    }

    public static <T extends DataAbstraction> List<T> listFrom(Cursor cursor, Class<T> type) {
        ArrayList<T> values = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                T value = newInstance(type);
                value.setInDb(true);
                value.populateFrom(cursor);
                values.add(value);
            }
            while(cursor.moveToNext());
        }

        return values;
    }

    private static <T extends DataAbstraction> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BadDataAbstractionException(e);
        }
    }

    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IN_DB, isInDb());
        bundle.putBoolean(MODIFIED, isModified());
        asBundle(bundle);
        return bundle;
    }

    protected abstract void asBundle(Bundle bundle);

    public abstract ContentValues asContentValues();

    protected abstract void populateFrom(Cursor cursor);

    protected abstract void populateFrom(Bundle bundle);

    public boolean isInDb() {
        return mInDb;
    }

    public void setInDb(boolean inDb) {
        this.mInDb = inDb;
    }

    public boolean isModified() {
        return mModified;
    }

    public void setModified(boolean modified) {
        this.mModified = modified;
    }
}
