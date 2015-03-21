package com.robwilliamson.db.utils;

import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.TimeZone;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class TimeTest {
    @Test
    public void testBundling() {
        Bundle bundle = new Bundle();
        String name = "testTime";
        DateTime input = DateTime.now().withZone(DateTimeZone.
                forTimeZone(TimeZone.getTimeZone("Germany/Berlin")));
        Utils.Time.bundle(bundle, name, input);
        DateTime output = Utils.Time.unBundle(bundle, name);
        Assert.assertTrue(input.isEqual(output));
        Assert.assertEquals(input, output);
    }
}
