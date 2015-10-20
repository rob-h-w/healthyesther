package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import javax.annotation.Nonnull;

public interface InitializationQuerier<R extends BaseRow> {
    @Nonnull
    TransactionExecutor.QueryOperation<R> getInitializationQuery();

    void onInitializationQueryResponse(@Nonnull R[] rows);
}
