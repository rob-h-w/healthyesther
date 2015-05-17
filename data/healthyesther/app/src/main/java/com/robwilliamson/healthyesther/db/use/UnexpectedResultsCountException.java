package com.robwilliamson.healthyesther.db.use;

public class UnexpectedResultsCountException extends RuntimeException {
    public UnexpectedResultsCountException(int count) {
        super("Received " + count + " results");
    }

    public UnexpectedResultsCountException(int expected, int actual) {
        super("Expected " + expected + ", but actually received " + actual + " results.");
    }
}
