package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.utils;

import android.os.Bundle;
import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.db.Utils;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class TimeTest extends AndroidTestCase {

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
}
