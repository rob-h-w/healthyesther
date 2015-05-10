package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Meal;

public class MealData extends DataAbstraction {
    public static class BadNameLength extends IllegalArgumentException {}

    public Long _id;
    public String name;

    public MealData(String name) {
        this(null, name);
    }

    public MealData(Long _id, String name) {
        this._id = _id == null || _id == 0L ? null : _id;

        if (!validateName(name)) {
            throw new BadNameLength();
        }
        this.name = name;
    }

    public static boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 140);
    }

    @Override
    public Bundle asBundle() {
        Bundle bundle = new Bundle();

        if (_id != null) {
            bundle.putLong(Meal._ID, _id);
        }

        bundle.putString(Meal.NAME, name);
        return bundle;
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();

        if (_id != null) {
            contentValues.put(Meal._ID, _id);
        }

        contentValues.put(Meal.NAME, name);
        return contentValues;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int _idIndex = cursor.getColumnIndex(Meal._ID);
        final int nameIndex = cursor.getColumnIndex(Meal.NAME);

        _id = cursor.getLong(_idIndex);
        name = cursor.getString(nameIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        _id = null;

        if (bundle.containsKey(Meal._ID)) {
            _id = bundle.getLong(Meal._ID);
        }

        name = bundle.getString(Meal.NAME);
    }
}
