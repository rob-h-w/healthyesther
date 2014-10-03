package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

/**
 * Table detailing event ids, times and types.
 */
public class Event extends Table {
    public enum Columns {
        _ID,
        WHEN,
        CREATED,
        MODIFIED,
        TYPE_ID,
        NAME
    }

    public Event(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getName() {
        return "event";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{
                "_id",
                "[when]",
                "created",
                "modified",
                "type_id",
                "name"
        };
    }

    @Override
    public void create() {
        db().execSQL("CREATE TABLE event ( \n" +
                "    _id      INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    [when]   DATETIME     NOT NULL\n" +
                "                          DEFAULT ( CURRENT_TIMESTAMP ),\n" +
                "    created  DATETIME     NOT NULL\n" +
                "                          DEFAULT ( CURRENT_TIMESTAMP ),\n" +
                "    modified DATETIME,\n" +
                "    type_id               REFERENCES event_type ( _id ) ON DELETE CASCADE\n" +
                "                                                        ON UPDATE CASCADE\n" +
                "                          NOT NULL,\n" +
                "    name     TEXT( 140 )  DEFAULT ( '' ) \n" +
                ");");
    }

    @Override
    public void upgrade(int from, int to) {
    }

    public void insert(Calendar when, int typeId, String name) {
        ContentValues values = new ContentValues();
        put(values, Columns.WHEN, when);
        put(values, Columns.TYPE_ID, typeId);
        put(values, Columns.NAME, name);
        insert(values);
    }
}
