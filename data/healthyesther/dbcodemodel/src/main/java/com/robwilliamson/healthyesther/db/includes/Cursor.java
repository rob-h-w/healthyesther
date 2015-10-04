package com.robwilliamson.healthyesther.db.includes;

public interface Cursor {
    Boolean getBoolean(String column);

    Double getDouble(String column);

    Long getLong(String column);

    String getString(String column);

    DateTime getDateTime(String column);

    void moveToFirst();

    boolean moveToNext();

    int count();
}
