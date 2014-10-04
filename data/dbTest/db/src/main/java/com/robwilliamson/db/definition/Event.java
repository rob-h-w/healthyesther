package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

/**
 * Table detailing event ids, times and types.
 */
public class Event extends Table {
    public final static String TABLE_NAME = "event";
    public final static String _ID = "_id";
    public final static String WHEN = "[when]";
    public final static String CREATED = "created";
    public final static String MODIFIED = "modified";
    public final static String TYPE_ID = "type_id";
    public final static String NAME = "name";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE event ( \n" +
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
    public void upgrade(SQLiteDatabase db, int from, int to) {
    }

    public void insert(SQLiteDatabase db, Calendar when, int typeId, String name) {
        ContentValues values = new ContentValues();
        values.put(WHEN, Time.toString(when));
        values.put(TYPE_ID, typeId);
        values.put(NAME, name);
        insert(db, values);
    }
}
