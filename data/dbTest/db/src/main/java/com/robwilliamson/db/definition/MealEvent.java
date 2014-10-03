package com.robwilliamson.db.definition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table containing details of a meal event.
 */
public class MealEvent extends Table {
    public MealEvent(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getName() {
        return "meal_event";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {
                "meal_id",
                "event_id",
                "amount",
                "units_id"
        };
    }

    @Override
    public void create() {
        db().execSQL("CREATE TABLE meal_event ( \n" +
                "    meal_id       NOT NULL\n" +
                "                  REFERENCES meal ( _id ) ON DELETE CASCADE\n" +
                "                                          ON UPDATE CASCADE,\n" +
                "    event_id      NOT NULL\n" +
                "                  REFERENCES event ( _id ) ON DELETE CASCADE\n" +
                "                                           ON UPDATE CASCADE,\n" +
                "    amount   REAL,\n" +
                "    units_id      REFERENCES units ( _id ) ON DELETE SET NULL\n" +
                "                                           ON UPDATE CASCADE,\n" +
                "    PRIMARY KEY ( meal_id, event_id ) \n" +
                ");\n");
    }

    @Override
    public void upgrade(int from, int to) {

    }

    public enum Columns {
        MEAL_ID,
        EVENT_ID,
        AMOUNT,
        UNITS_ID
    }
}
