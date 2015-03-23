package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.util.time;

import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.util.time.Range;
import com.robwilliamson.healthyesther.util.time.RangeSet;
import com.robwilliamson.healthyesther.util.time.TimeRegion;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

public class RangeSetTest extends AndroidTestCase {
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
    private static final Range HUGE_RANGE = new Range(FROM.minus(Duration.standardDays(1)), TO.plus(Duration.standardDays(1)));

    private static final RangeSet SMALL_RANGES = new RangeSet(SMALL_FROM_RANGE, SMALL_CENTRE_RANGE, SMALL_TO_RANGE);
    private static final RangeSet SMALL_RANGES_FUTURE_EDGES = new RangeSet(
            SMALL_FROM_RANGE.starting(SMALL_FROM_RANGE.to),
            SMALL_CENTRE_RANGE.starting(SMALL_CENTRE_RANGE.to),
            SMALL_TO_RANGE.starting(SMALL_TO_RANGE.to));

    public void testOverlaps() {
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES));
        assertTrue(SMALL_RANGES.overlaps(FROM_EDGE_RANGE));
        assertTrue(SMALL_RANGES.overlaps(TO_EDGE_RANGE));
        assertTrue(SMALL_RANGES_FUTURE_EDGES.overlaps(SMALL_RANGES));
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES_FUTURE_EDGES));
    }

    public void testOverlapsInclusive() {
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES, TimeRegion.Comparison.INCLUSIVE));
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES_FUTURE_EDGES, TimeRegion.Comparison.INCLUSIVE));
    }

    public void testOverlapsExclusive() {
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES, TimeRegion.Comparison.EXCLUSIVE));
        assertFalse(SMALL_RANGES.overlaps(SMALL_RANGES_FUTURE_EDGES, TimeRegion.Comparison.EXCLUSIVE));
    }

    public void testIsIn() {
        assertTrue(HUGE_RANGE.contains(SMALL_RANGES));
    }
}
