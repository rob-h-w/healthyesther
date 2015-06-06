package com.robwilliamson.healthyesther.db.definition;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;

/**
 * Performs either an insert or an update of one or more table elements.
 */
public abstract class Modification implements RowIdProvider {
    private Long mRowId;

    @Override
    public final Long getRowId() {
        return mRowId;
    }

    protected final void setRowId(long rowId) {
        if (Utils.equals(mRowId, rowId)) {
            return;
        }

        mRowId = rowId;
        onRowIdChanged(rowId);
    }

    protected final boolean isRowIdSet() {
        return mRowId != null;
    }

    public void modify(SQLiteDatabase db) {
        onStartModify(db);
        DataAbstraction data = getData();
        if (data == null) {
            onNullModify(db);
        } else {
            if (!data.isInDb()) {
                setRowId(insert(db));
            } else if (data.isModified()) {
                update(db);
            }
        }
        onEndModify(db);
    }

    protected void onRowIdChanged(Long rowId) {}
    protected void onStartModify(SQLiteDatabase db) {}
    protected void onNullModify(SQLiteDatabase db) {}
    protected void onEndModify(SQLiteDatabase db) {}

    protected abstract DataAbstraction getData();
    protected abstract void update(SQLiteDatabase db);
    protected abstract long insert(SQLiteDatabase db);
}
