package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.reminder;

import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.reminder.TimingModel;
import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import static com.robwilliamson.healthyesther.unit.Assert.assertIsEqual;

public class TimingModelTest extends AndroidTestCase {
    private static final DateTime DAY = new DateTime(2015, 3, 21, 7, 0);
    private static final DateTime NIGHT = new DateTime(2015, 3, 21, 22, 0);
    private static final Range DISALLOWED = new Range(DAY, NIGHT);
    private MockTimingModelEnvironment mEnvironment;
    private TimingModel mSubject;

    private static class MockTimingModelEnvironment implements TimingModel.Environment {
        public DateTime lastNotifiedTime = null;

        @Override
        public void setLastNotifiedTime(DateTime time) {
            this.lastNotifiedTime = time;
        }

        @Override
        public DateTime getLastNotifiedTime() {
            return this.lastNotifiedTime;
        }

        @Override
        public DateTime getNextNotificationTime() {
            return null;
        }

        @Override
        public boolean appInForeground() {
            return false;
        }

        @Override
        public void setAlarm(DateTime alarmTime) {

        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mEnvironment = new MockTimingModelEnvironment();
        mSubject = new TimingModel(
                mEnvironment,
                Duration.standardSeconds(10),
                Duration.standardSeconds(1),
                DISALLOWED);
    }

    public void testOnNotified() {
        mSubject.onNotified();
        assertIsEqual(DateTime.now(), Duration.standardSeconds(1), mEnvironment.lastNotifiedTime);
    }
}
