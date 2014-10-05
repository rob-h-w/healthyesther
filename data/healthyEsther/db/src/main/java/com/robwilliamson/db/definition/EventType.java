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

        insert(db, 1, "Meal", null);
        insert(db, 2, "Take medication", null);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }

    private void insert(SQLiteDatabase db, int _id, String name, String icon) {
        ContentValues values = new ContentValues();
        values.put(_ID, _id);
        values.put(NAME, name);
        values.put(ICON, icon);
        insert(db, values);
    }
}
