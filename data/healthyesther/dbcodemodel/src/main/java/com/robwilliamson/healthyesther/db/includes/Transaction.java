package com.robwilliamson.healthyesther.db.includes;

import java.util.List;

public interface Transaction {
    public void addCompletionHandler(CompletionHandler handler);


    public String execSQL(String sql);

    public long insert(List<String> columnNames, Object... columnValues);

    public int update(Where where, List<String> columnNames, Object... columnValues);

    public int remove(Where where);

    public static interface CompletionHandler {
        public void onCompleted();
    }
}
