package com.robwilliamson.healthyesther.db.use;

public interface QueryUserProvider {

    /**
     * An array of query users.
     *
     * @return The query users, or an empty array if no query is required.
     */
    QueryUser[] getQueryUsers();
}
