package com.robwilliamson.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Represents a custom query.
 */
public interface Query {
    public Cursor query(SQLiteDatabase db);
    public void onQueryComplete(Cursor cursor);
}
