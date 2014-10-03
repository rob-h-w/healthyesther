package com.robwilliamson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.EventType;
import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.db.definition.MealEvent;
import com.robwilliamson.db.definition.Table;
import com.robwilliamson.db.definition.Units;

/**
 * Contains database table and column names, and other database-definition data.
 */
public final class Contract {
    private static volatile Contract sContract = null;
    private final SQLiteDatabase mDb;

    private Contract(SQLiteDatabase db) {
        mDb = db;

        EVENT = new Event(mDb);
        EVENT_TYPE = new EventType(mDb);
        MEAL = new Meal(mDb);
        MEAL_EVENT = new MealEvent(mDb);
        UNITS = new Units(mDb);

        TABLES = new Table[] {
                EVENT,
                EVENT_TYPE,
                MEAL_EVENT,
                MEAL,
                UNITS,
        };
    }

    public static synchronized Contract getInstance(SQLiteDatabase db) {
        if (sContract == null) {
            sContract = new Contract(db);
        }

        return sContract;
    }

    public static final String NAME = "health.db3";
    public static final int VERSION = 1;

    public final Event EVENT;
    public final EventType EVENT_TYPE;
    public final Meal MEAL;
    public final MealEvent MEAL_EVENT;
    public final Units UNITS;

    public final Table[] TABLES;

    public void create() {
        for (Table table : TABLES) {
            table.create();
        }
    }

    public void delete() {
        for (Table table : TABLES) {
            table.delete();
        }
    }

    public void upgrade(int from, int to) {
        for (Table table : TABLES) {
            table.upgrade(from, to);
        }
    }
}
