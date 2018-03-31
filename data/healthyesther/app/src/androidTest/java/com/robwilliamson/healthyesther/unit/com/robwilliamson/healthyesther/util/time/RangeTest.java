/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.util.time;

import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.util.time.Range;
import com.robwilliamson.healthyesther.util.time.TimeRegion;
import com.robwilliamson.healthyesther.util.time.TimeRegion.Comparison;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.robwilliamson.healthyesther.unit.Assert.assertIsEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RangeTest {
    private static final ZonedDateTime FROM = ZonedDateTime.of(
            2010,
            4,
            19,
            13,
            0,
            0,
            0,
            ZoneOffset.UTC);
    private static final ZonedDateTime TO = ZonedDateTime.of(
            2010,
            4,
            19,
            14,
            0,
            0,
            0,
            ZoneOffset.UTC);
    private static final ZonedDateTime CENTRE = ZonedDateTime.of(
            2010,
            4,
            19,
            13,
            30,
            0,
            0,
            ZoneOffset.UTC);
    private static final Duration SIGMA = Duration.ofMinutes(30);
    private static final Duration SMALL_SIGMA = Duration.ofMinutes(1);

    private static final Range SMALL_FROM_RANGE = new Range(FROM, SMALL_SIGMA);
    private static final Range SMALL_CENTRE_RANGE = new Range(CENTRE, SMALL_SIGMA);
    private static final Range SMALL_TO_RANGE = new Range(TO, SMALL_SIGMA);
    private static final Range FROM_EDGE_RANGE = new Range(FROM.minus(SMALL_SIGMA), FROM);
    private static final Range TO_EDGE_RANGE = new Range(TO, TO.plus(SMALL_SIGMA));

    private Range mSubject;

    @SuppressWarnings("RedundantThrows")
    @Before
    public void setUp() throws Exception {
        mSubject = new Range(FROM, TO);
    }

    @Test
    public void testFromToConstructor() {
        checkTimingIsCorrect(new Range(FROM, TO));
    }

    @Test
    public void testTransposedFromToConstructor() {
        checkTimingIsCorrect(new Range(TO, FROM));
    }

    @Test
    public void testCentreSigmaConstructor() {
        checkTimingIsCorrect(new Range(CENTRE, SIGMA));
    }

    @Test
    public void testInRangeDefault() {
        assertTrue(mSubject.contains(FROM));
        assertTrue(mSubject.contains(CENTRE));
        assertTrue(mSubject.contains(TO));
        assertTrue(mSubject.contains(mSubject));
        assertFalse(mSubject.contains(SMALL_FROM_RANGE));
        assertTrue(mSubject.contains(SMALL_CENTRE_RANGE));
        assertFalse(mSubject.contains(SMALL_TO_RANGE));
    }

    @Test
    public void testInRangeInclusive() {
        assertTrue(mSubject.contains(FROM, Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(TO, Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(CENTRE, Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(mSubject, Comparison.INCLUSIVE));
        assertFalse(mSubject.contains(SMALL_FROM_RANGE, Comparison.INCLUSIVE));
        assertTrue(mSubject.contains(SMALL_CENTRE_RANGE, Comparison.INCLUSIVE));
        assertFalse(mSubject.contains(SMALL_TO_RANGE, Comparison.INCLUSIVE));
    }

    @Test
    public void testInRangeExclusive() {
        assertFalse(mSubject.contains(FROM, Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(TO, Comparison.EXCLUSIVE));
        assertTrue(mSubject.contains(CENTRE, Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(mSubject, Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(SMALL_FROM_RANGE, Comparison.EXCLUSIVE));
        assertTrue(mSubject.contains(SMALL_CENTRE_RANGE, Comparison.EXCLUSIVE));
        assertFalse(mSubject.contains(SMALL_TO_RANGE, Comparison.EXCLUSIVE));
    }

    @Test
    public void testRangeOverlaps() {
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE));
        assertTrue(mSubject.overlaps(SMALL_CENTRE_RANGE));
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE));
        assertTrue(mSubject.overlaps(FROM_EDGE_RANGE));
        assertTrue(mSubject.overlaps(TO_EDGE_RANGE));
    }

    @Test
    public void testRangeOverlapsInclusive() {
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_CENTRE_RANGE, Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(FROM_EDGE_RANGE, Comparison.INCLUSIVE));
        assertTrue(mSubject.overlaps(TO_EDGE_RANGE, Comparison.INCLUSIVE));
    }

    @Test
    public void testRangeOverlapsExclusive() {
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Comparison.EXCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_CENTRE_RANGE, Comparison.EXCLUSIVE));
        assertTrue(mSubject.overlaps(SMALL_FROM_RANGE, Comparison.EXCLUSIVE));
        assertFalse(mSubject.overlaps(FROM_EDGE_RANGE, Comparison.EXCLUSIVE));
        assertFalse(mSubject.overlaps(TO_EDGE_RANGE, Comparison.EXCLUSIVE));
        assertTrue(mSubject.overlaps(new Range(FROM, TO.plus(SIGMA)), Comparison.EXCLUSIVE));
    }

    @Test
    public void testStarting() {
        Duration difference = Duration.ofMillis(
                TO.toInstant().toEpochMilli() - FROM.toInstant().toEpochMilli());
        Range subject = mSubject.startingFrom(TO);
        assertIsEqual(TO, subject.from);
        assertIsEqual(TO.plus(difference), subject.to);
        assertIsEqual(CENTRE.plus(difference), subject.centre);
        assertEquals(SIGMA, subject.sigma);
    }

    @Test
    public void testStartingYesterday() {
        Range subject = mSubject.startingYesterday();
        assertIsEqual(FROM.minus(Duration.ofDays(1)), subject.from);
        assertIsEqual(TO.minus(Duration.ofDays(1)), subject.to);
    }

    @Test
    public void testStartingTomorrow() {
        Range subject = mSubject.startingTomorrow();
        assertIsEqual(FROM.plus(Duration.ofDays(1)), subject.from);
        assertIsEqual(TO.plus(Duration.ofDays(1)), subject.to);
    }

    @Test
    public void testStartingFromDate() {
        TimeRegion subject = mSubject.startingFrom(2016, 7, 13);
        assertTrue(subject.contains(FROM
                .withYear(2016)
                .withMonth(7)
                .withDayOfMonth(13)));
    }

    private void checkTimingIsCorrect(Range subject) {
        assertIsEqual(FROM, subject.from);
        assertIsEqual(TO, subject.to);
        assertIsEqual(CENTRE, subject.centre);
        assertEquals(SIGMA, subject.sigma);
    }
}
