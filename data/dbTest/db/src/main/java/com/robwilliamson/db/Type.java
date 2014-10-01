package com.robwilliamson.db;

/**
 * SQLite data types.
 */
public enum Type {
    INTEGER,
    DATETIME,
    TEXT;

    public String toString(Integer maximumLength) {
        if (maximumLength == null) {
            return toString();
        }

        return toString() + "( " + maximumLength + " )";
    }
}
