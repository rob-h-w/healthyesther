package com.robwilliamson.db.definition;

import android.database.sqlite.SQLiteDatabase;

/**
 * Performs either an insert or an update of one or more table elements.
 */
public abstract class Modification {
    private Long mRowId;

    public final Long getRowId() {
        return mRowId;
    }

    protected final void setRowId(long rowId) {
        mRowId = rowId;
    }

    protected final boolean isRowIdSet() {
        return mRowId != null;
    }

    public abstract void modify(SQLiteDatabase db);
}
