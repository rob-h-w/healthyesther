package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by rob on 04/10/14.
 */
public class Medication extends Table {
    public static final String TABLE_NAME = "medication";
    public static final String _ID = "_id";
    public static final String NAME = "name";

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
