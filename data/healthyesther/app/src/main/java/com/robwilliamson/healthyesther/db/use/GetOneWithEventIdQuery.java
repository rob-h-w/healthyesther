package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.Contract;

public abstract class GetOneWithEventIdQuery extends GetOneWithColumnIdQuery {
    public GetOneWithEventIdQuery(long id) {
        super(id);
    }

    @Override
    public String getIdColumnName() {
        return Contract.getInstance().EVENT.getForeignIdName();
    }
}
