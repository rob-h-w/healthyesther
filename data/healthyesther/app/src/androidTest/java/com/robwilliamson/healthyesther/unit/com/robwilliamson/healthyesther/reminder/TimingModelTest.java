/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.reminder;

import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.reminder.TimingModel;
import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.robwilliamson.healthyesther.unit.Assert.assertIsEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class TimingModelTest {
    private static final DateTime MORNING_8AM_21 = new DateTime(2015, 3, 21, 8, 0).withZone(DateTimeZone.UTC);
    private static final DateTime MORNING_21 = new DateTime(2015, 3, 21, 7, 0);
    private static final DateTime MIDDAY_21 = new DateTime(2015, 3, 21, 12, 0);
    private static final DateTime EVENING_21 = new DateTime(2015, 3, 21, 22, 0);
    private static final DateTime MIDNIGHT_21 = new DateTime(2015, 3, 21, 0, 0);
    private static final Range ALLOWED = new Range(MORNING_21, EVENING_21);
    private static final Duration PERIOD = Duration.standardHours(1);
    private static final Duration HALF_PERIOD = Duration.standardMinutes(30);
    private static final Duration MIN_NOTIFICATION_SEPARATION = Duration.standardMinutes(30);

    private MockTimingModelEnvironment mEnvironment;
    private TimingModel mSubject;

    @Before
    public void setUp() throws Exception {
        mEnvironment = new MockTimingModelEnvironment();
        mSubject = new TimingModel(
                mEnvironment,
                PERIOD,
                MIN_NOTIFICATION_SEPARATION,
                ALLOWED);
    }

    @Test
    public void testOnAlarmElapsed_inDisallowedRange() {
        mEnvironment.now = MIDNIGHT_21;
        mSubject.onAlarmElapsed();
        assertIsEqual(MORNING_21, mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
    }

    @Test
    public void testOnAlarmElapsed_inAllowedRange() {
        mEnvironment.now = MORNING_21;
        mSubject.onAlarmElapsed();
        assertIsEqual(MORNING_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(1, mEnvironment.sendReminderCallCount);
    }

    @Test
    public void testOnAlarmElapsed_doesNotChangeNextAfterPrematureElapsed() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.plus(HALF_PERIOD);
        mEnvironment.lastNotifiedTime = MIDDAY_21.minus(HALF_PERIOD);
        mSubject.onAlarmElapsed();
        assertIsEqual(MIDDAY_21.plus(HALF_PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
        assertNull(mEnvironment.setLastNotifiedTimeParams);
    }

    @Test
    public void testOnAlarmElapsed_isIdempotent() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.plus(HALF_PERIOD);
        mEnvironment.lastNotifiedTime = MIDDAY_21.minus(HALF_PERIOD);
        mSubject.onAlarmElapsed();

        assertIsEqual(MIDDAY_21.plus(HALF_PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
        assertNull(mEnvironment.setLastNotifiedTimeParams);

        mEnvironment.now = MIDDAY_21.plus(Duration.standardMinutes(1));
        mSubject.onAlarmElapsed();
        mSubject.onAlarmElapsed();

        assertIsEqual(MIDDAY_21.plus(HALF_PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
        assertNull(mEnvironment.setLastNotifiedTimeParams);
    }

    @Test
    public void testEnsureNotificationIsPending_nextIsSet() throws Exception {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.plus(Duration.standardMinutes(1));
        ensureNotificationIsPending(mSubject);

        assertIsEqual(mEnvironment.nextNotificationTime, mEnvironment.setNextNotificationTimeParams.alarmTime);
    }

    @Test
    public void testEnsureNotificationIsPending_nextIsNull() throws Exception {
        mEnvironment.now = MIDDAY_21;
        ensureNotificationIsPending(mSubject);

        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setNextNotificationTimeParams.alarmTime);
    }

    @Test
    public void testEnsureNotificationIsPending_nextIsTooFarInTheFuture() throws Exception {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.withYear(2016);
        ensureNotificationIsPending(mSubject);

        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setNextNotificationTimeParams.alarmTime);
    }

    @Test
    public void testEnsureNotificationIsPending_nextIsInThePast() throws Exception {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.minus(HALF_PERIOD);
        ensureNotificationIsPending(mSubject);

        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setNextNotificationTimeParams.alarmTime);
    }

    @Test
    public void testOnApplicationCreated_nightNoNotificationsSet() {
        mEnvironment.now = MIDDAY_21;
        mSubject.onApplicationCreated();

        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnBootCompleted() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.setLastNotifiedTime(MORNING_21.minus(Duration.standardDays(1)));
        mSubject.onBootCompleted();

        assertEquals(1, mEnvironment.sendReminderCallCount);
        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnNotified() {
        mSubject.onNotified();
        assertIsEqual(MORNING_8AM_21, Duration.standardSeconds(1), mEnvironment.setLastNotifiedTimeParams.time);
        assertIsEqual(MORNING_8AM_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnScreenOn() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.setLastNotifiedTime(MORNING_21.minus(Duration.standardDays(1)));
        mSubject.onScreenOn();

        assertEquals(1, mEnvironment.sendReminderCallCount);
        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnUserEntry() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.setLastNotifiedTime(MIDDAY_21.minus(HALF_PERIOD));
        mEnvironment.appInForeground = true;
        mSubject.onUserEntry();
        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
    }

    private void ensureNotificationIsPending(TimingModel model) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = TimingModel.class.getDeclaredMethod("ensureNotificationIsPending");
        method.setAccessible(true);
        method.invoke(model);
    }

    private static class MockTimingModelEnvironment implements TimingModel.Environment {
        public DateTime now = MORNING_8AM_21;
        public boolean appInForeground = false;
        public DateTime lastNotifiedTime = null;
        public DateTime nextNotificationTime = null;

        public SetLastNotifiedTimeParams setLastNotifiedTimeParams = null;
        public SetNextNotificationTimeParams setNextNotificationTimeParams = null;
        public SetAlarmParams setAlarmParams = null;
        public int sendReminderCallCount = 0;

        @Override
        public DateTime getNow() {
            return now;
        }

        @Override
        public DateTime getLastNotifiedTime() {
            if (lastNotifiedTime != null) {
                return lastNotifiedTime;
            }

            return setLastNotifiedTimeParams == null ? null : setLastNotifiedTimeParams.time;
        }

        @Override
        public void setLastNotifiedTime(final DateTime time) {
            setLastNotifiedTimeParams = new SetLastNotifiedTimeParams();
            setLastNotifiedTimeParams.time = time;
        }

        @Override
        public DateTime getNextNotificationTime() {
            if (nextNotificationTime != null) {
                return nextNotificationTime;
            }

            return setNextNotificationTimeParams == null ? null : setNextNotificationTimeParams.alarmTime;
        }

        @Override
        public void setNextNotificationTime(DateTime time) {
            setNextNotificationTimeParams = new SetNextNotificationTimeParams();
            setNextNotificationTimeParams.alarmTime = time;
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

        public static class SetLastNotifiedTimeParams {
            public DateTime time;
        }

        public static class SetNextNotificationTimeParams {
            public DateTime alarmTime;
        }

        public static class SetAlarmParams {
            public DateTime alarmTime;
        }
    }
}
