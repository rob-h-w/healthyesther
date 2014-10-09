package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Utils;

import org.joda.time.DateTime;

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

    public long insert(SQLiteDatabase db, DateTime when, long typeId, String name) {
        ContentValues values = new ContentValues();
        values.put(WHEN, Utils.Time.toDatabaseString(when));
        values.put(TYPE_ID, typeId);
        values.put(NAME, name);
        return insert(db, values);
    }

    public int update(SQLiteDatabase db, long id, DateTime when, String name) {
        ContentValues values = new ContentValues();
        values.put(WHEN, Utils.Time.toDatabaseString(when));
        values.put(MODIFIED, Utils.Time.toDatabaseString(DateTime.now()));
        values.put(NAME, name);
        return update(db, values, id);
    }

    public boolean validateName(String name) {
        return Utils.Strings.validateLength(name, 1, 140);
    }
}
