package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.util.time;

import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.util.time.Range;
import com.robwilliamson.healthyesther.util.time.RangeSet;
import com.robwilliamson.healthyesther.util.time.TimeRegion;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RangeSetTest {
    private static final DateTime FROM = new DateTime(2010, 4, 19, 13, 0, 0, 0, DateTimeZone.UTC);
    private static final DateTime TO = new DateTime(2010, 4, 19, 14, 0, 0, 0, DateTimeZone.UTC);
    private static final DateTime CENTRE = new DateTime(2010, 4, 19, 13, 30, 0, 0, DateTimeZone.UTC);
    private static final Duration SMALL_SIGMA = Duration.standardMinutes(1);

    private static final Range SMALL_FROM_RANGE = new Range(FROM, SMALL_SIGMA);
    private static final Range SMALL_CENTRE_RANGE = new Range(CENTRE, SMALL_SIGMA);
    private static final Range SMALL_TO_RANGE = new Range(TO, SMALL_SIGMA);
    private static final Range FROM_EDGE_RANGE = new Range(FROM.minus(SMALL_SIGMA), FROM);
    private static final Range TO_EDGE_RANGE = new Range(TO, TO.plus(SMALL_SIGMA));
    private static final Range HUGE_RANGE = new Range(FROM.minus(Duration.standardDays(1)), TO.plus(Duration.standardDays(1)));

    private static final RangeSet SMALL_RANGES = new RangeSet(SMALL_FROM_RANGE, SMALL_CENTRE_RANGE, SMALL_TO_RANGE);
    private static final RangeSet SMALL_RANGES_FUTURE_EDGES = new RangeSet(
            SMALL_FROM_RANGE.startingFrom(SMALL_FROM_RANGE.to),
            SMALL_CENTRE_RANGE.startingFrom(SMALL_CENTRE_RANGE.to),
            SMALL_TO_RANGE.startingFrom(SMALL_TO_RANGE.to));

    @Test
    public void testOverlaps() {
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES));
        assertTrue(SMALL_RANGES.overlaps(FROM_EDGE_RANGE));
        assertTrue(SMALL_RANGES.overlaps(TO_EDGE_RANGE));
        assertTrue(SMALL_RANGES_FUTURE_EDGES.overlaps(SMALL_RANGES));
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES_FUTURE_EDGES));
    }

    @Test
    public void testOverlapsInclusive() {
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES, TimeRegion.Comparison.INCLUSIVE));
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES_FUTURE_EDGES, TimeRegion.Comparison.INCLUSIVE));
    }

    @Test
    public void testOverlapsExclusive() {
        assertTrue(SMALL_RANGES.overlaps(SMALL_RANGES, TimeRegion.Comparison.EXCLUSIVE));
        assertFalse(SMALL_RANGES.overlaps(SMALL_RANGES_FUTURE_EDGES, TimeRegion.Comparison.EXCLUSIVE));
    }

    @Test
    public void testIsIn() {
        assertTrue(HUGE_RANGE.contains(SMALL_RANGES));
    }

    @Test
    public void testStartingFromDate() {
        RangeSet subject = new RangeSet(HUGE_RANGE).startingFrom(2015, 4, 18);
        assertFalse(subject.contains(FROM));
        assertTrue(subject.contains(FROM.withDate(2015, 4, 18)));
    }

    @Test
    public void testNextEdge() {
        ReadableInstant nextEdge = SMALL_RANGES.getEdgeAfter(TO.plus(Duration.standardDays(1)));
        assertNull(nextEdge);

        nextEdge = SMALL_RANGES.getEdgeAfter(SMALL_RANGES.to);
        assertEquals(SMALL_RANGES.to, nextEdge);

        nextEdge = SMALL_RANGES.getEdgeAfter(TO);
        assertEquals(SMALL_RANGES.to, nextEdge);

        nextEdge = SMALL_RANGES.getEdgeAfter(SMALL_TO_RANGE.from.minus(Duration.standardSeconds(1)));
        assertEquals(SMALL_TO_RANGE.from, nextEdge);
    }
}
