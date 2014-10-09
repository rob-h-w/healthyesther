package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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

    protected long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(getName(), null, values);
    }

    protected int update(SQLiteDatabase db, ContentValues values, long id) {
        return update(db, values, "_id", id);
    }

    protected int update(SQLiteDatabase db, ContentValues values, String idName, long id) {
        return db.update(getName(), values, "? = ?", new String[] { idName, String.valueOf(id) });
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
}
