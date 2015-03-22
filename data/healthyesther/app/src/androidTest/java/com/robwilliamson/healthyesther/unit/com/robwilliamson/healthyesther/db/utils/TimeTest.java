package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.utils;

import android.os.Bundle;
import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.db.Utils;

import junit.framework.Assert;

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
        Assert.assertTrue(input.isEqual(output));
        Assert.assertEquals(input.getZone(), output.getZone());
        Assert.assertTrue(input.getZone().equals(output.getZone()));
    }

    public void testLocalNow() {
        DateTime localNow = Utils.Time.localNow();
        Assert.assertEquals(localNow.getZone(), DateTimeZone.forOffsetMillis(
                TimeZone.getDefault().getRawOffset()));
    }

    public void testToLocalStringUtc() {
        Assert.assertEquals(UTC_STRING, Utils.Time.toLocalString(UTC));
    }

    public void testToLocalStringCet() {
        Assert.assertEquals(CET_STRING, Utils.Time.toLocalString(CET));
    }

    public void testFromLocalStringUtc() {
        fromLocalString(UTC_STRING, UTC);
    }

    public void testFromLocalStringCet() {
        fromLocalString(CET_STRING, CET);
    }

    private void fromLocalString(String string, DateTime expected) {
        DateTime actual = Utils.Time.fromLocalString(string);
        Assert.assertEquals(actual.getZone(), DateTimeZone.getDefault());
        Assert.assertTrue(actual.isEqual(expected));
    }
}
