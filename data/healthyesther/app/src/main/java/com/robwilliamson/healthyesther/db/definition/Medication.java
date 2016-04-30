package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.MedicationData;

public class Medication extends Table {
    public static final String TABLE_NAME = "medication";
    public static final String _ID = "_id";
    public static final String NAME = "name";

    public static boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 50);
    }

    public static void checkName(String name) {
        if (!Medication.validateName(name)) {
            throw new IllegalArgumentException("Medication name must be 1-50 characters long.");
        }
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

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private MedicationData mValue;

        public Modification(long rowId) {
            setRowId(rowId);
        }

        public Modification(MedicationData data) {
            mValue = data;
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
            Contract.getInstance().MEDICATION.update(db, mValue.asContentValues(), mValue.get_id());
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().MEDICATION.insert(db, mValue.asContentValues());
        }
    }
}
