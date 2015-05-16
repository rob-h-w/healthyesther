package com.robwilliamson.healthyesther.db.definition;

public class SimpleRowIdProvider implements RowIdProvider {
    private final long mRowId;

    public SimpleRowIdProvider(long rowId) {
        mRowId = rowId;
    }

    @Override
    public Long getRowId() {
        return mRowId;
    }
}
