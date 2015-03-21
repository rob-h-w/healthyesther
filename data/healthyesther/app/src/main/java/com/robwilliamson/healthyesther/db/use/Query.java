package com.robwilliamson.healthyesther.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Represents a custom query.
 */
public interface Query {
    /**
     * Does the actual query. Transactions should begin and end within this method.
     * @param db
     * @return
     */
    public Cursor query(final SQLiteDatabase db);

    /**
     * Called after the query in the worker thread. Use this to parse the cursor output.
     * The query has been successfully completed if this method is called.
     * {@link #onQueryComplete(android.database.Cursor)} has not yet been called.<br/>
     * If this method throws, no further callbacks will be called; the thrown object will not be
     * handled.
     * @param cursor
     */
    public void postQueryProcessing(final Cursor cursor);

    /**
     * Called in the UI thread after successful processing of the query.
     * @param cursor
     */
    public void onQueryComplete(final Cursor cursor);

    /**
     * Called in the UI thread after processing the query threw. It is assumed that the query was
     * rolled back.
     * @param error
     */
    public void onQueryFailed(final Throwable error);
}
