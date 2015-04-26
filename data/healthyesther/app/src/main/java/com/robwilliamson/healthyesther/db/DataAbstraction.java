package com.robwilliamson.healthyesther.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public abstract class DataAbstraction {

    public static class BadDataAbstractionException extends RuntimeException {
        public BadDataAbstractionException(Throwable causedBy) {
            super("Implementations must have accessible no-value constructors.", causedBy);
        }
    }

    public static <T extends DataAbstraction> T from(Bundle bundle, Class<T> type) {
        T value = newInstance(type);
        value.populateFrom(bundle);
        return value;
    }

    public static <T extends DataAbstraction> List<T> listFrom(Cursor cursor, Class<T> type) {
        ArrayList<T> values = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                T value = newInstance(type);

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

    public abstract Bundle asBundle();

    public abstract ContentValues asContentValues();

    protected abstract void populateFrom(Cursor cursor);

    protected abstract void populateFrom(Bundle bundle);
}
