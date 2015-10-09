package com.robwilliamson.healthyesther.db.includes;

import java.util.List;

public interface Transaction {
    void addCompletionHandler(CompletionHandler handler);

    String execSQL(String sql);

    long insert(List<String> columnNames, Object... columnValues);

    int update(Where where, List<String> columnNames, Object... columnValues);

    int remove(Where where);

    void commit();

    interface CompletionHandler {
        void onCompleted();
    }
}
