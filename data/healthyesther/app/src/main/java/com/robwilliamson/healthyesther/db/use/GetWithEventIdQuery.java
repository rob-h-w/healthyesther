package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.Contract;

public abstract class GetWithEventIdQuery extends GetSomeValuesQuery {
    private final long mId;

    public GetWithEventIdQuery(long id) {
        mId = id;
    }

    @Override
    public String getWhereSelection() {
        return Contract.getInstance().EVENT.getForeignIdName() + " = " + String.valueOf(mId);
    }

    @Override
    public String[] getResultColumns() {
        return null;
    }
}
