package com.robwilliamson.healthyesther.unit;

import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import static junit.framework.Assert.assertTrue;

public final class Assert {
    public static void assertIsEqual(DateTime expected, DateTime actual) {
        assertTrue("Expected time " + expected +
                " is equal to actual time " + actual, expected.isEqual(actual));
    }

    public static void assertIsEqual(DateTime expected, Duration sigma, DateTime actual) {
        Range range = new Range(expected, sigma);
        assertTrue("Expected time to be about " + expected + ", actually was " + actual,
                range.contains(actual));
    }
}
