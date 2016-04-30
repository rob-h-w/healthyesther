package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.reminder;

import android.test.AndroidTestCase;

import com.robwilliamson.healthyesther.reminder.TimingModel;
import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import static com.robwilliamson.healthyesther.unit.Assert.assertIsEqual;

public class TimingModelTest extends AndroidTestCase {
    private static final DateTime MORNING_8AM_21 = new DateTime(2015, 3, 21, 8, 0).withZone(DateTimeZone.UTC);
    private static final DateTime MORNING_21 = new DateTime(2015, 3, 21, 7, 0);
    private static final DateTime MIDDAY_21 = new DateTime(2015, 3, 21, 12, 0);
    private static final DateTime EVENING_21 = new DateTime(2015, 3, 21, 22, 0);
    private static final DateTime MIDNIGHT_21 = new DateTime(2015, 3, 21, 0, 0);
    private static final Range ALLOWED = new Range(MORNING_21, EVENING_21);
    private static final Duration PERIOD = Duration.standardHours(1);
    private static final Duration MIN_NOTIFICATION_SEPARATION = Duration.standardMinutes(30);

    private MockTimingModelEnvironment mEnvironment;
    private TimingModel mSubject;

    private static class MockTimingModelEnvironment implements TimingModel.Environment {
        public DateTime now = MORNING_8AM_21;
        public boolean appInForeground = false;

        public SetLastNotifiedTimeParams setLastNotifiedTimeParams = null;
        public SetAlarmParams setAlarmParams = null;
        public int sendReminderCallCount = 0;

        public static class SetLastNotifiedTimeParams {
            public DateTime time;
        }

        public static class SetAlarmParams {
            public DateTime alarmTime;
        }

        @Override
        public DateTime getNow() {
            return now;
        }

        @Override
        public void setLastNotifiedTime(final DateTime time) {
            setLastNotifiedTimeParams = new SetLastNotifiedTimeParams();
            setLastNotifiedTimeParams.time = time;
        }

        @Override
        public DateTime getLastNotifiedTime() {
            return setLastNotifiedTimeParams == null ? null : setLastNotifiedTimeParams.time;
        }

        @Override
        public DateTime getNextNotificationTime() {
            return null;
        }

        @Override
        public boolean appInForeground() {
            return appInForeground;
        }

        @Override
        public void setAlarm(DateTime alarmTime) {
            setAlarmParams = new SetAlarmParams();
            setAlarmParams.alarmTime = alarmTime;
        }

        @Override
        public void sendReminder() {
            sendReminderCallCount++;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mEnvironment = new MockTimingModelEnvironment();
        mSubject = new TimingModel(
                mEnvironment,
                PERIOD,
                MIN_NOTIFICATION_SEPARATION,
                ALLOWED);
    }

    public void testOnNotified() {
        mSubject.onNotified();
        assertIsEqual(MORNING_8AM_21, Duration.standardSeconds(1), mEnvironment.setLastNotifiedTimeParams.time);
    }

    public void testOnAlarmExpired_inDisallowedRange() {
        mEnvironment.now = MIDNIGHT_21;
        mSubject.onAlarmExpired();
        assertIsEqual(MORNING_21, mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
    }
}
