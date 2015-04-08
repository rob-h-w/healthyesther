package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.utils;

import android.os.Bundle;
import android.os.SystemClock;
import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class TimeTest extends AndroidTestCase {
    private final DateTime UTC = new DateTime(2015, 3, 22, 8, 56, 25, 0, DateTimeZone.UTC);
    private final String UTC_STRING = "2015-03-22T08:56:25 +00:00";
    private final DateTime CET = new DateTime(2015, 3, 22, 8, 56, 25, 0, DateTimeZone.forID("+0100"));
    private final String CET_STRING = "2015-03-22T08:56:25 +01:00";

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

    public void testLocalNow() {
        DateTime localNow = Utils.Time.localNow();
        assertEquals(localNow.getZone(), DateTimeZone.forTimeZone(
                TimeZone.getDefault()));
    }

    public void testToLocalStringUtc() {
        assertEquals(UTC_STRING, Utils.Time.toLocalString(UTC));
    }

    public void testToLocalStringCet() {
        assertEquals(CET_STRING, Utils.Time.toLocalString(CET));
    }

    public void testToDatabaseStringUTC() {
        assertEquals(UTC_STRING, Utils.Time.toDatabaseString(UTC));
    }

    public void testToDatabaseStringCet() {
        assertEquals(CET_STRING, Utils.Time.toDatabaseString(CET));
    }

    public void testFromDatabaseStringUtc() {
        fromDatabaseString(UTC_STRING, UTC);
    }

    public void testFromDatabaseStringCet() {
        fromDatabaseString(CET_STRING, CET);
    }

    private void fromDatabaseString(String string, DateTime expected) {
        DateTime actual = Utils.Time.fromDatabaseString(string);
        assertEquals(expected.getZone(), actual.getZone());
        assertTrue("Expect " + expected + " to equal " + actual, actual.isEqual(expected));
    }

    public void testFromLocalStringUtc() {
        checkFromLocalString(UTC_STRING, UTC);
    }

    public void testFromLocalStringCet() {
        checkFromLocalString(CET_STRING, CET);
    }

    private void checkFromLocalString(String string, DateTime expected) {
        DateTime actual = Utils.Time.fromLocalString(string);
        assertEquals(expected.getZone(), actual.getZone());
        assertTrue("Expect " + expected + " to equal " + actual, actual.isEqual(expected));
    }

    public void testToBootRealTimeElapsedMillis() {
        long millisTime = SystemClock.elapsedRealtime();
        assertEquals(millisTime, Utils.Time.toBootRealTimeElapsedMillis(DateTime.now()), 1000);
    }
}
