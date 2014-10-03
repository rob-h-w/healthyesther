package com.robwilliamson.db.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Units describing quantities. These are used with events to track amounts of food eaten, chemicals
 * comprising food and medications, and wherever else appropriate.
 */
public class Units extends Table {
    public enum Columns {
        _Id,
        Name
    }

    public Units(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getName() {
        return "units";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {
            "_id",
            "name"
        };
    }

    @Override
    public void create() {
        db().execSQL("CREATE TABLE units ( \n" +
                "    _id  INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 40 )  NOT NULL\n" +
                "                     UNIQUE \n" +
                ");\n");
    }

    @Override
    public void upgrade(int from, int to) {

    }
}
