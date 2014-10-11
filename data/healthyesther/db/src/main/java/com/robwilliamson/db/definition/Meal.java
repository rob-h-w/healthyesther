package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;

/**
 * Meal table contains all unique types of meal.
 */
public class Meal extends Table {
    public static class Modification extends com.robwilliamson.db.definition.Modification {
        private String mName;

        public Modification(String name) {
            if (!validateName(name)) {
                throw new IllegalArgumentException("A meal must have a name that is less than 141 characters.");
            }

            mName = name;
        }

        public Modification(long id) {
            setRowId(id);
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().MEAL.insert(db, mName));
            }
        }
    }

    public static final String TABLE_NAME = "meal";
    public static final String _ID = "_id";
    public static final String NAME = "name";

    public static boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 140);
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE meal ( \n" +
                "    _id  INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 140 )  NOT NULL\n" +
                "                      UNIQUE \n" +
                ");\n");
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
