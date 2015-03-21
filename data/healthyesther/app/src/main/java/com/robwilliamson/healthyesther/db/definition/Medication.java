package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;

public class Medication extends Table {
    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private String mName;

        public Modification(long rowId) {
            setRowId(rowId);
        }

        public Modification(String name) {
            if (!validateName(name)) {
                throw new IllegalArgumentException("Medication name must be 1-50 characters long.");
            }

            mName = name;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().MEDICATION.insert(db, mName));
            }
        }
    }

    public static final String TABLE_NAME = "medication";
    public static final String _ID = "_id";
    public static final String NAME = "name";

    public static boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 50);
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medication ( \n" +
                "    _id  INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 50 )  NOT NULL \n" +
                ");");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }

    public long insert(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        return insert(db, values);
    }
}
