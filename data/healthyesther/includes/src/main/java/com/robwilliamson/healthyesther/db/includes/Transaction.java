package com.robwilliamson.healthyesther.db.includes;

import java.util.List;

import javax.annotation.Nonnull;

public interface Transaction extends AutoCloseable {
    void addCompletionHandler(@Nonnull CompletionHandler handler);

    @Override
    void close();

    void execSQL(@Nonnull String sql);

    long insert(@Nonnull String table, @Nonnull List<String> columnNames, @Nonnull Object... columnValues);

    int update(@Nonnull String table, @Nonnull Where where, @Nonnull List<String> columnNames, @Nonnull Object... columnValues);

    int remove(@Nonnull String table, @Nonnull Where where);

    void commit();

    void rollBack();

    interface CompletionHandler {
        void onCompleted();
    }
}
