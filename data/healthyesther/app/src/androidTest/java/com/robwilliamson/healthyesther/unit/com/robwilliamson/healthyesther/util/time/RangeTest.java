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

    private Range mSubject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSubject = new Range(FROM, TO);
    }

    public void testFromToConstructor() {
        checkTimingIsCorrect(new Range(FROM, TO));
    }

    public void testTransposedFromToConstructor() {
        checkTimingIsCorrect(new Range(TO, FROM));
    }

    public void testCentreSigmaConstructor() {
        checkTimingIsCorrect(new Range(CENTRE, SIGMA));
    }

    public void testInRangeDefault() {
        assertTrue(mSubject.contains(FROM));
        assertTrue(mSubject.contains(TO));
    }

    public void testInRange() {
        assertTrue(mSubject.contains(CENTRE));
    }

    public void testInRangeInclusive() {
        assertTrue(mSubject.contains(FROM, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(TO, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(CENTRE, Range.Comparison.INCLUSIVE));
    }

    public void testInRangeExclusive() {
        assertFalse(mSubject.contains(FROM, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(TO, Range.Comparison.EXCLUSIVE));
        assertTrue(mSubject.contains(CENTRE, Range.Comparison.EXCLUSIVE));
    }

    private void checkTimingIsCorrect(Range subject) {
        assertIsEqual(FROM, subject.from);
        assertIsEqual(TO, subject.to);
        assertIsEqual(CENTRE, subject.centre);
        assertEquals(SIGMA, subject.sigma);
    }
}
