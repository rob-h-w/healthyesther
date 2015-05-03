package com.robwilliamson.healthyesther.db.use;

public abstract class GetAllValuesQuery extends GetSomeValuesQuery {

    @Override
    public String getWhereSelection() {
        return null;
    }
}
