package com.robwilliamson.healthyesther.db.use;

import android.database.Cursor;

public abstract class GetOneWithColumnIdQuery extends GetWithColumnIdQuery {

    protected abstract void postQueryProcessOne(Cursor cursor);

    public GetOneWithColumnIdQuery(long id) {
        super(id);
    }

    @Override
    public String getOrder() {
        return null;
    }

    @Override
    public String getOrderColumn() {
        return null;
    }

    /**
     * Called after the query in the worker thread. Use this to parse the cursor output.
     * The query has been successfully completed if this method is called.
     * {@link #onQueryComplete(Cursor)} has not yet been called.<br/>
     * If this method throws, no further callbacks will be called; the thrown object will not be
     * handled.
     *
     * @param cursor
     */
    @Override
    public void postQueryProcessing(Cursor cursor) {
        if (cursor.getCount() > 1) {
            throw new UnexpectedResultsCountException(1, cursor.getCount(), cursor);
        }

        postQueryProcessOne(cursor);
    }
}
