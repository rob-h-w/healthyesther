package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.HealthDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Details of a particular SQLite table.
 */
public abstract class Table {
    private final SQLiteDatabase mDb;

    public static class NotWritableException extends RuntimeException {};

    public Table(SQLiteDatabase db) {
        if (db.isReadOnly()) {
            throw new NotWritableException();
        }
        mDb = db;
    }

    public abstract String getName();
    public abstract String[] getColumnNames();

    public abstract void create();
    public abstract void upgrade(int from, int to);
    public void delete() {
        db().execSQL("drop table if exists " + getName());
    }

    protected String getColumnName(Enum e) {
        return getColumnNames()[e.ordinal()];
    }

    protected void insert(ContentValues values) {
        db().insert(getName(), null, values);
    }

    protected void put(ContentValues values, Enum e, int value) {
        values.put(getColumnName(e), value);
    }

    protected void put(ContentValues values, Enum e, String value) {
        values.put(getColumnName(e), value);
    }

    protected void put(ContentValues values, Enum e, Calendar value) {
        values.put(getColumnName(e), Time.toString(value));
    }

    protected SQLiteDatabase db() {
        return mDb;
    }

    protected static final class Time{
        private static String sFormat = "yyyy-MM-dd HH:mm:ss";

        public static String toString(Calendar calendar) {
            SimpleDateFormat format = new SimpleDateFormat(sFormat);
            return format.format(calendar.getTime());
        }

        public static Calendar fromString(String string) throws ParseException {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("utf-8"));
            SimpleDateFormat format = new SimpleDateFormat(sFormat, Locale.ROOT);
            calendar.setTime(format.parse(string));
            return calendar;
        }
    }
}
