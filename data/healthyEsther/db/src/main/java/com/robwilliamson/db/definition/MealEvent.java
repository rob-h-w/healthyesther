package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table containing details of a meal event.
 */
public class MealEvent extends Table {
    public static final long EVENT_TYPE_ID = 1;
    public static final String TABLE_NAME = "meal_event";
    public static final String MEAL_ID = "meal_id";
    public static final String EVENT_ID = "event_id";
    public static final String AMOUNT = "amount";
    public static final String UNITS_ID = "units_id";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE meal_event ( \n" +
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
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }

    public void insert(SQLiteDatabase db, long meal_id, long event_id) {
        insert(db, meal_id, event_id, null, null);
    }

    public void insert(SQLiteDatabase db, long meal_id, long event_id, Double amount, Long units_id) {
        ContentValues values = new ContentValues();
        values.put(MEAL_ID, meal_id);
        values.put(EVENT_ID, event_id);

        if (amount != null) {
            values.put(AMOUNT, amount);
        }

        if (units_id != null) {
            values.put(UNITS_ID, units_id);
        }

        insert(db, values);
    }
}
