package com.robwilliamson.healthyesther.db.includes;

import java.util.List;

public interface Transaction {
    public void addCompletionHandler(CompletionHandler handler);

    public long insert(List<String> columnNames, Object... columnValues);

    public static interface CompletionHandler {
        public void onCompleted();
    }
}
