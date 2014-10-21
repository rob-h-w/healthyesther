package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Table describing the type of each event.
 */
public final class EventType extends Table {
    public static final String TABLE_NAME = "event_type";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String ICON = "icon";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE event_type ( \n" +
                "    _id  INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 30 )  NOT NULL\n" +
                "                     UNIQUE,\n" +
                "    icon TEXT \n" +
                ");\n");

        insert(db, MealEvent.EVENT_TYPE_ID, "Meal", null);
        insert(db, MedicationEvent.EVENT_TYPE_ID, "Take medication", null);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from == 1) {
            insert(db, HealthScoreEvent.EVENT_TYPE_ID, "Health & mood", null);
        }
    }

    private void insert(SQLiteDatabase db, long _id, String name, String icon) {
        ContentValues values = new ContentValues();
        values.put(_ID, _id);
        values.put(NAME, name);
        values.put(ICON, icon);
        insert(db, values);
    }
}
