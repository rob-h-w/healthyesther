/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit;

import com.robwilliamson.healthyesther.util.time.Range;

import java.time.Duration;
import java.time.ZonedDateTime;

import static junit.framework.Assert.assertTrue;

public final class Assert {
    public static void assertIsEqual(ZonedDateTime expected, ZonedDateTime actual) {
        assertTrue("Expected time " + expected +
                " is equal to actual time " + actual, expected.isEqual(actual));
    }

    public static void assertIsEqual(ZonedDateTime expected, Duration sigma, ZonedDateTime actual) {
        Range range = new Range(expected, sigma);
        assertTrue("Expected time to be about " + expected + ", actually was " + actual,
                range.contains(actual));
    }
}
