package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import javax.annotation.Nonnull;

public interface InitializationQuerier {
    @Nonnull
    TransactionExecutor.Operation getInitializationQuery();
}
