package com.robwilliamson.healthyesther.db.includes;

public interface Cursor {
    public long getLong(String column);

    public String getString(String column);

    public DateTime getTime(String column);

    public void moveToFirst();

    public boolean moveToNext();

    public int count();
}
