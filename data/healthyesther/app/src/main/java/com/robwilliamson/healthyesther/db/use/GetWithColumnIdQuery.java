package com.robwilliamson.healthyesther.db.use;

public abstract class GetWithColumnIdQuery extends GetSomeValuesQuery {
    private final long mId;

    public abstract String getIdColumnName();

    public GetWithColumnIdQuery(long id) {
        mId = id;
    }

    @Override
    public String[] getResultColumns() {
        return null;
    }

    @Override
    public String getWhereSelection() {
        return getIdColumnName() + " = " + String.valueOf(mId);
    }
}
