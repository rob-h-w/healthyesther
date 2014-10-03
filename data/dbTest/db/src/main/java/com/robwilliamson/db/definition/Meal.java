package com.robwilliamson.db.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Meal table contains all unique types of meal.
 */
public class Meal extends Table {
    public enum Columns {
        _Id,
        Name
    }

    public Meal(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getName() {
        return "meal";
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
        db().execSQL("CREATE TABLE meal ( \n" +
                "    _id  INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 140 )  NOT NULL\n" +
                "                      UNIQUE \n" +
                ");\n");
    }

    @Override
    public void upgrade(int from, int to) {

    }
}
