package com.robwilliamson.healthyesther.db.use;

public abstract class GetWithColumnIdQuery extends GetSomeValuesQuery {
    private final long mId;

    public GetWithColumnIdQuery(long id) {
        mId = id;
    }

    public abstract String getIdColumnName();

    @Override
    public String[] getResultColumns() {
        return null;
    }

    @Override
    public String getWhereSelection() {
        return getIdColumnName() + " = " + String.valueOf(mId);
    }
}
