package com.robwilliamson.healthyesther.unit;

import org.joda.time.DateTime;

public final class Assert {
    public static void assertIsEqual(DateTime expected, DateTime actual) {
        junit.framework.Assert.assertTrue("Expected time " + expected +
                " is equal to actual time " + actual, expected.isEqual(actual));
    }
}
