package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.utils;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TimeTest {
    private final DateTime UTC = new DateTime(2015, 3, 22, 8, 56, 25, 0, DateTimeZone.UTC);
    private final String UTC_STRING = "2015-03-22T08:56:25 +00:00";
    private final DateTime CET = new DateTime(2015, 3, 22, 8, 56, 25, 0, DateTimeZone.forID("+0100"));
    private final String CET_STRING = "2015-03-22T08:56:25 +01:00";

    @Test
    public void testBundling() {
        Bundle bundle = new Bundle();
        String name = "testTime";
        DateTime input = DateTime.now().withZone(
                DateTimeZone.forOffsetMillis(
                        TimeZone.getDefault().getRawOffset()));
        System.out.println(input.getZone());
        Utils.Time.bundle(bundle, name, input);
        DateTime output = Utils.Time.unBundle(bundle, name);
        assertTrue(input.isEqual(output));
        assertEquals(input.getZone(), output.getZone());
        assertTrue(input.getZone().equals(output.getZone()));
    }

    @Test
    public void testLocalNow() {
        DateTime localNow = Utils.Time.localNow();
        assertEquals(localNow.getZone(), DateTimeZone.forTimeZone(
                TimeZone.getDefault()));
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

    private void fromDatabaseString(String string, DateTime expected) {
        DateTime actual = Utils.Time.fromDatabaseString(string);
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

    private void checkFromLocalString(String string, DateTime expected) {
        DateTime actual = Utils.Time.fromLocalString(string);
        assertEquals(expected.getZone(), actual.getZone());
        assertTrue("Expect " + expected + " to equal " + actual, actual.isEqual(expected));
    }

    @Test
    public void testToBootRealTimeElapsedMillis() {
        long millisTime = SystemClock.elapsedRealtime();
        assertEquals(millisTime, Utils.Time.toBootRealTimeElapsedMillis(DateTime.now()), 1000);
    }
}
