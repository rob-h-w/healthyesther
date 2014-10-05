package com.robwilliamson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.EventType;
import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.db.definition.MealEvent;
import com.robwilliamson.db.definition.Medication;
import com.robwilliamson.db.definition.MedicationEvent;
import com.robwilliamson.db.definition.Table;
import com.robwilliamson.db.definition.Units;

/**
 * Contains database table and column names, and other database-definition data.
 */
public final class Contract {
    private static volatile Contract sContract = null;

    private static class NotInitializedException extends NullPointerException {}

    private Contract() {
        EVENT = new Event();
        EVENT_TYPE = new EventType();
        MEAL = new Meal();
        MEAL_EVENT = new MealEvent();
        MEDICATION = new Medication();
        MEDICATION_EVENT = new MedicationEvent();
        UNITS = new Units();

        TABLES = new Table[] {
                EVENT,
                EVENT_TYPE,
                MEAL_EVENT,
                MEAL,
                MEDICATION_EVENT,
                MEDICATION,
                UNITS,
        };
    }

    public static synchronized Contract getInstance() {
        initialize();
        return sContract;
    }

    private static synchronized void initialize() {
        if (sContract == null) {
            sContract = new Contract();
        }
    }

    public static final String NAME = "health.db3";
    public static final int VERSION = 1;

    public final Event EVENT;
    public final EventType EVENT_TYPE;
    public final Meal MEAL;
    public final MealEvent MEAL_EVENT;
    public final Medication MEDICATION;
    public final MedicationEvent MEDICATION_EVENT;
    public final Units UNITS;

    public final Table[] TABLES;

    public void create(SQLiteDatabase db) {
        for (Table table : TABLES) {
            table.create(db);
        }
    }

    public void delete(SQLiteDatabase db) {
        for (Table table : TABLES) {
            table.delete(db);
        }
    }

    public void upgrade(SQLiteDatabase db, int from, int to) {
        for (Table table : TABLES) {
            table.upgrade(db, from, to);
        }
    }
}
