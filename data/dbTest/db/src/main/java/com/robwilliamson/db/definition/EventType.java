package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.jar.Attributes;

/**
 * Table describing the type of each event.
 */
public final class EventType extends Table {
    public enum Columns {
        _Id,
        Name,
        Icon
    }

    public EventType(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getName() {
        return "event_type";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {
                "_id",
                "name",
                "icon"
        };
    }

    @Override
    public void create() {
        db().execSQL("CREATE TABLE event_type ( \n" +
                "    _id  INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 30 )  NOT NULL\n" +
                "                     UNIQUE,\n" +
                "    icon TEXT \n" +
                ");\n");

        insert(1, "Eat a meal", null);
        insert(2, "Take medication", null);
    }

    @Override
    public void upgrade(int from, int to) {

    }

    private void insert(int _id, String name, String icon) {
        ContentValues values = new ContentValues();
        put(values, Columns._Id, _id);
        put(values, Columns.Name, name);
        put(values, Columns.Icon, icon);
        insert(values);
    }
}
