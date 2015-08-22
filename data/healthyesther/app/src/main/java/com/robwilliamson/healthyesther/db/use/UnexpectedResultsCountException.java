package com.robwilliamson.healthyesther.db.use;

import android.database.Cursor;

public class UnexpectedResultsCountException extends RuntimeException {
    private String mMessage;

    public UnexpectedResultsCountException(int count) {
        super("Received " + count + " results");
    }

    public UnexpectedResultsCountException(int expected, int actual, Cursor cursor) {
        super("Expected " + expected + ", but actually received " + actual + ".");
        String[] columnNames = cursor.getColumnNames();
        StringBuilder columns = new StringBuilder();
        String separator = "";
        for (String columnName : columnNames) {
            columns.append(separator).append(columnName);
            separator = ", ";
        }
        mMessage = "Expected " + expected +
                ", but actually received " + actual +
                " results with these columns: " + columns + ".";
    }

    /**
     * Returns the detail message which was provided when this
     * {@code Throwable} was created. Returns {@code null} if no message was
     * provided at creation time.
     */
    @Override
    public String getMessage() {
        if (mMessage == null) {
            return super.getMessage();
        }

        return mMessage;
    }
}
