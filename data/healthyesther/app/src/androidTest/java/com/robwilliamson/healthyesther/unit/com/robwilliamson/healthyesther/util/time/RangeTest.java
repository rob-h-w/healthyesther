package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.util.time;

import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import static com.robwilliamson.healthyesther.unit.Assert.assertIsEqual;

public class RangeTest extends AndroidTestCase {
    private static final DateTime FROM = new DateTime(2010, 4, 19, 13, 0, 0, 0, DateTimeZone.UTC);
    private static final DateTime TO = new DateTime(2010, 4, 19, 14, 0, 0, 0, DateTimeZone.UTC);
    private static final DateTime CENTRE = new DateTime(2010, 4, 19, 13, 30, 0, 0, DateTimeZone.UTC);
    private static final Duration SIGMA = Duration.standardMinutes(30);

    public void testFromToConstructor() {
        checkTimingIsCorrect(new Range(FROM, TO));
    }

    public void testCentreSigmaConstructor() {
        checkTimingIsCorrect(new Range(CENTRE, SIGMA));
    }

    private void checkTimingIsCorrect(Range subject) {
        assertIsEqual(FROM, subject.from);
        assertIsEqual(TO, subject.to);
        assertIsEqual(CENTRE, subject.centre);
        assertEquals(SIGMA, subject.sigma);
    }
}
