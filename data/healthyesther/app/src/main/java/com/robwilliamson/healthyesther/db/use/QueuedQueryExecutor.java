package com.robwilliamson.healthyesther.db.use;

import java.util.List;

public interface QueuedQueryExecutor {
    void enqueueQueries(List<Query> queries);
}
