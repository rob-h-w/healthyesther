package com.robwilliamson.healthyesther.db.includes;

import java.util.List;

public interface Transaction {
    public static interface CompletionHandler {
        public void onCompleted();
    }

    public void registerCompletionHandler(CompletionHandler handler);

    public long insert(List<String> columnNames, Object... columnValues);
}
