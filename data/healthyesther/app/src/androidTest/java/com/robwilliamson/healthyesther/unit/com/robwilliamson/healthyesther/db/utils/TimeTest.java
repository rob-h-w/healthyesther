/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.utils;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.db.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TimeTest {
    private final ZonedDateTime UTC = ZonedDateTime.of(
            2015,
            3,
            22,
            8,
            56,
            25,
            0,
            ZoneOffset.UTC);
    private final String UTC_STRING = "2015-03-22T08:56:25 +00:00";
    private final ZonedDateTime CET = ZonedDateTime.of(
            2015,
            3,
            22,
            8,
            56,
            25,
            0,
            ZoneId.of("+0100"));
    private final String CET_STRING = "2015-03-22T08:56:25 +01:00";

    @Test
    public void testBundling() {
        Bundle bundle = new Bundle();
        String name = "testTime";
        ZonedDateTime input = ZonedDateTime.now()
                .withZoneSameLocal(ZoneId.systemDefault())
                .withFixedOffsetZone()
                .withNano(0);
        Utils.Time.bundle(bundle, name, input);
        ZonedDateTime output = Utils.Time.unBundle(bundle, name);
        assertTrue(input.isEqual(output));
        assertEquals(input.getZone(), output.getZone());
        assertTrue(input.getZone().equals(output.getZone()));
    }

    @Test
    public void testLocalNow() {
        ZonedDateTime localNow = Utils.Time.localNow();
        assertEquals(localNow.getZone(), ZoneId.systemDefault());
    }

    @Test
    public void testToLocalStringUtc() {
        assertEquals(UTC_STRING, Utils.Time.toLocalString(UTC));
    }

    @Test
    public void testToLocalStringCet() {
        assertEquals(CET_STRING, Utils.Time.toLocalString(CET));
    }

    @Test
    public void testToDatabaseStringUTC() {
        assertEquals(UTC_STRING, Utils.Time.toDatabaseString(UTC));
    }

    @Test
    public void testToDatabaseStringCet() {
        assertEquals(CET_STRING, Utils.Time.toDatabaseString(CET));
    }

    @Test
    public void testFromDatabaseStringUtc() {
        fromDatabaseString(UTC_STRING, UTC);
    }

    @Test
    public void testFromDatabaseStringCet() {
        fromDatabaseString(CET_STRING, CET);
    }

    private void fromDatabaseString(String string, ZonedDateTime expected) {
        ZonedDateTime actual = Utils.Time.fromDatabaseString(string);
        assertEquals(expected.getZone(), actual.getZone());
        assertTrue("Expect " + expected + " to equal " + actual, actual.isEqual(expected));
    }

    @Test
    public void testFromLocalStringUtc() {
        checkFromLocalString(UTC_STRING, UTC);
    }

    @Test
    public void testFromLocalStringCet() {
        checkFromLocalString(CET_STRING, CET);
    }

    private void checkFromLocalString(String string, ZonedDateTime expected) {
        ZonedDateTime actual = Utils.Time.fromLocalString(string);
        assertEquals(expected.getZone(), actual.getZone());
        assertTrue("Expect " + expected + " to equal " + actual, actual.isEqual(expected));
    }

    @Test
    public void testToBootRealTimeElapsedMillis() {
        long millisTime = SystemClock.elapsedRealtime();
        assertEquals(millisTime, Utils.Time.toBootRealTimeElapsedMillis(ZonedDateTime.now()), 1000);
    }
}
