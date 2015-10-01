package com.robwilliamson.healthyesther.db.includes;

public interface Table {
    public void create(Transaction transaction);

    public void drop(Transaction transaction);
}
