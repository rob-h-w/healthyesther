package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.NoteData;

public class Note extends Table {
    public static final String TABLE_NAME = "note";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String NOTE = "note";

    public boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 140);
    }

    public boolean validateNote(String note) {
        return true;
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE note ( \n" +
                "    _id  INTEGER      PRIMARY KEY,\n" +
                "    name TEXT( 140 )  NOT NULL,\n" +
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

    private void update(SQLiteDatabase db, long rowId, String note) {
        ContentValues values = new ContentValues();
        values.put(NOTE, note);
        update(db, values, rowId);
    }

    public static class BadNameLength extends IllegalArgumentException {
    }

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private final NoteData mValue;

        public Modification(NoteData value) {
            setRowId(value.get_id());
            mValue = value;
        }

        public Modification(String name, String note) {
            mValue = new NoteData();
            mValue.setName(name);
            mValue.setNote(note);
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
            Contract.getInstance().NOTE.update(db, mValue.asContentValues(), mValue.get_id());
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().NOTE.insert(db, mValue.asContentValues());
        }
    }
}
