package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Details of a particular SQLite table.
 */
public abstract class Table {
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

    public abstract String getName();

    public abstract void create(SQLiteDatabase db);

    public abstract void upgrade(SQLiteDatabase db, int from, int to);

    public void delete(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + getName());
    }

    public String getForeignIdName() {
        return getName() + "_id";
    }

    protected long insert(SQLiteDatabase db, ContentValues values) {
        return db.insertOrThrow(getName(), null, values);
    }

    protected int update(SQLiteDatabase db, ContentValues values, long id) {
        return update(db, values, "_id", id);
    }

    protected int update(SQLiteDatabase db, ContentValues values, String idName, long id) {
        return update(db, values, idName, id, 1, 1);
    }

    protected int update(SQLiteDatabase db, ContentValues values, String idName, long id, int minRows, int maxRows) {
        return update(db, values, idName + " = " + String.valueOf(id), minRows, maxRows);
    }

    protected int update(SQLiteDatabase db, ContentValues values, String where, int minRows, int maxRows) {
        int rows = db.update(getName(), values, where, null);

        if (rows < minRows) {
            throw new SQLiteException("Expected update to affect at least " + minRows + " rows, but it actually affected " + rows + " rows.");
        }

        if (rows > maxRows) {
            throw new SQLiteException("Expected update to affect at most " + maxRows + " rows, but it actually affected " + rows + " rows.");
        }

        return rows;
    }

    public String getQualifiedName(String name) {
        return getQualifiedName(getName(), name);
    }
}
