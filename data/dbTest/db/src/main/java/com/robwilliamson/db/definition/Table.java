package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.HealthDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Details of a particular SQLite table.
 */
public abstract class Table {
    public abstract String getName();

    public abstract void create(SQLiteDatabase db);
    public abstract void upgrade(SQLiteDatabase db, int from, int to);
    public void delete(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + getName());
    }

    protected void insert(SQLiteDatabase db, ContentValues values) {
        db.insert(getName(), null, values);
    }

    public static String[] cleanName(String[] names) {
        for (int i = 0; i < names.length; i++) {
            names[i] = cleanName(names[i]);
        }

        return names;
    }
    public static String cleanName(String name) {
        // Strip square brackets, if present.
        if (name.startsWith("[")) {
            name = name.substring(1);
        }

        if (name.endsWith("]")) {
            name = name.substring(0, name.length() - 1);
        }

        return name;
    }

    public static String getQualifiedName(String qualifier, String name) {
        return qualifier + "." + name;
    }

    public static String[] getQualifiedName(String qualifier, String names[], int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex + 1; i++) {
            String name = names[i];
            names[i] = getQualifiedName(qualifier, name);
        }

        return names;
    }

    public String getQualifiedName(String name) {
        return getQualifiedName(getName(), name);
    }

    public static final class Time{
        private static final String sFormat = "yyyy-MM-dd HH:mm:ss";

        public static String toString(Calendar calendar) {
            SimpleDateFormat format = new SimpleDateFormat(sFormat);
            return format.format(calendar.getTime());
        }

        public static Calendar fromString(String string) throws ParseException {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("utc"));
            SimpleDateFormat format = new SimpleDateFormat(sFormat, Locale.ROOT);
            calendar.setTime(format.parse(string));
            return calendar;
        }
    }
}
