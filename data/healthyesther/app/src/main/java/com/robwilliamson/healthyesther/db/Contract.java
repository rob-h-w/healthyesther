package com.robwilliamson.healthyesther.db;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.EventType;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.definition.HealthScoreEvent;
import com.robwilliamson.healthyesther.db.definition.Meal;
import com.robwilliamson.healthyesther.db.definition.MealEvent;
import com.robwilliamson.healthyesther.db.definition.Medication;
import com.robwilliamson.healthyesther.db.definition.MedicationEvent;
import com.robwilliamson.healthyesther.db.definition.MedicationName;
import com.robwilliamson.healthyesther.db.definition.Note;
import com.robwilliamson.healthyesther.db.definition.NoteEvent;
import com.robwilliamson.healthyesther.db.definition.Table;
import com.robwilliamson.healthyesther.db.definition.Units;

/**
 * Contains database table and column names, and other database-definition data.
 */
public final class Contract {
    private static volatile Contract sContract = null;

    private static class NotInitializedException extends NullPointerException {}

    private Contract() {
        EVENT = new Event();
        EVENT_TYPE = new EventType();
        HEALTH_SCORE = new HealthScore();
        HEALTH_SCORE_EVENT = new HealthScoreEvent();
        MEAL = new Meal();
        MEAL_EVENT = new MealEvent();
        MEDICATION = new Medication();
        MEDICATION_NAME = new MedicationName();
        MEDICATION_EVENT = new MedicationEvent();
        NOTE = new Note();
        NOTE_EVENT = new NoteEvent();
        UNITS = new Units();

        TABLES = new Table[] {
                EVENT,
                EVENT_TYPE,
                HEALTH_SCORE_EVENT,
                HEALTH_SCORE,
                MEAL_EVENT,
                MEAL,
                MEDICATION_EVENT,
                MEDICATION,
                MEDICATION_NAME,
                NOTE_EVENT,
                NOTE,
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
    public static final int VERSION = 3;

    public final Event EVENT;
    public final EventType EVENT_TYPE;
    public final HealthScore HEALTH_SCORE;
    public final HealthScoreEvent HEALTH_SCORE_EVENT;
    public final Meal MEAL;
    public final MealEvent MEAL_EVENT;
    public final Medication MEDICATION;
    public final MedicationName MEDICATION_NAME;
    public final MedicationEvent MEDICATION_EVENT;
    public final Note NOTE;
    public final NoteEvent NOTE_EVENT;
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
