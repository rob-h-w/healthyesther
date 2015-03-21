package com.robwilliamson.healthyesther.reminder;

import com.robwilliamson.healthyesther.reminder.TimingManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", emulateSdk = 18)
public class TimingManagerTest {
    @Test
    public void testInstantiation() throws Exception {
        assertTrue(TimingManager.INSTANCE != null);
    }

}