package com.robwilliamson.db.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Meal table contains all unique types of meal.
 */
public class Meal extends Table {
    public static final String TABLE_NAME = "meal";
    public static final String _ID = "_id";
    public static final String NAME = "name";

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
}
