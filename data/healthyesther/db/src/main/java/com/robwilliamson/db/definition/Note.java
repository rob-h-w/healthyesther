package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;

public class Note extends Table {
    public static class BadNameLength extends IllegalArgumentException {}
    public static class Modification extends com.robwilliamson.db.definition.Modification {

        private final String mName;
        private final String mNote;

        public Modification(String name, String note) {
            mName = name;
            mNote = note;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().NOTE.insert(db, mName, mNote));
            }
        }
    }

    public static final String TABLE_NAME = "note";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String NOTE = "note";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE note ( \n" +
                "    _id  INTEGER      PRIMARY KEY,\n" +
                "    name TEXT( 140 )  NOT NULL\n" +
                "                      UNIQUE,\n" +
                "    note TEXT \n" +
                ");\n");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from == 2) {
            create(db);
        }
    }

    public long insert(SQLiteDatabase db, String name, String note) {
        if (!Utils.Strings.validateLength(name, 1, 140)) {
            throw new BadNameLength();
        }

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NOTE, note);
        return insert(db, values);
    }
}
