package com.robwilliamson.db.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Units describing quantities. These are used with events to track amounts of food eaten, chemicals
 * comprising food and medications, and wherever else appropriate.
 */
public class Units extends Table {
    public static final String TABLE_NAME = "units";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String SI_FACTOR = "si_factor";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE units ( \n" +
                "    _id       INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "    name      TEXT( 40 )  NOT NULL\n" +
                "                          UNIQUE,\n" +
                "    si_factor REAL \n" +
                ");\n");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }
}
