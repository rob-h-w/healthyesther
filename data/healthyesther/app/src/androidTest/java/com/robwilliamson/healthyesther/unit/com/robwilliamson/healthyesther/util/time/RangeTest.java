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
    private static final Duration SMALL_SIGMA = Duration.standardMinutes(1);

    private static final Range SMALL_FROM_RANGE = new Range(FROM, SMALL_SIGMA);
    private static final Range SMALL_CENTRE_RANGE = new Range(CENTRE, SMALL_SIGMA);
    private static final Range SMALL_TO_RANGE = new Range(TO, SMALL_SIGMA);
    private static final Range FROM_EDGE_RANGE = new Range(FROM.minus(SMALL_SIGMA), FROM);
    private static final Range TO_EDGE_RANGE = new Range(TO, TO.plus(SMALL_SIGMA));

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
        assertTrue(mSubject.contains(CENTRE));
        assertTrue(mSubject.contains(TO));
        assertTrue(mSubject.contains(mSubject));
        assertFalse(mSubject.contains(SMALL_FROM_RANGE));
        assertTrue(mSubject.contains(SMALL_CENTRE_RANGE));
        assertFalse(mSubject.contains(SMALL_TO_RANGE));
    }

    public void testInRangeInclusive() {
        assertTrue(mSubject.contains(FROM, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(TO, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(CENTRE, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(mSubject, Range.Comparison.INCLUSIVE));
        assertFalse(mSubject.contains(SMALL_FROM_RANGE, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(SMALL_CENTRE_RANGE, Range.Comparison.INCLUSIVE));
        assertFalse(mSubject.contains(SMALL_TO_RANGE, Range.Comparison.INCLUSIVE));
    }

    public void testInRangeExclusive() {
        assertFalse(mSubject.contains(FROM, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(TO, Range.Comparison.EXCLUSIVE));
        assertTrue(mSubject.contains(CENTRE, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(mSubject, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(SMALL_FROM_RANGE, Range.Comparison.EXCLUSIVE));
        assertTrue(mSubject.contains(SMALL_CENTRE_RANGE, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(SMALL_TO_RANGE, Range.Comparison.EXCLUSIVE));
    }

    public void testRangeOverlaps() {
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE));
        assertTrue(mSubject.overlaps(SMALL_CENTRE_RANGE));
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE));
        assertTrue(mSubject.overlaps(FROM_EDGE_RANGE));
        assertTrue(mSubject.overlaps(TO_EDGE_RANGE));
    }

    public void testRangeOverlapsInclusive() {
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_CENTRE_RANGE, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(FROM_EDGE_RANGE, Range.Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(TO_EDGE_RANGE, Range.Comparison.INCLUSIVE));
    }

    public void testRangeOverlapsExclusive() {
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Range.Comparison.EXCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_CENTRE_RANGE, Range.Comparison.EXCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.overlaps(FROM_EDGE_RANGE, Range.Comparison.EXCLUSIVE));
        assertFalse(mSubject.overlaps(TO_EDGE_RANGE, Range.Comparison.EXCLUSIVE));
    }

    private void checkTimingIsCorrect(Range subject) {
        assertIsEqual(FROM, subject.from);
        assertIsEqual(TO, subject.to);
        assertIsEqual(CENTRE, subject.centre);
        assertEquals(SIGMA, subject.sigma);
    }
}
