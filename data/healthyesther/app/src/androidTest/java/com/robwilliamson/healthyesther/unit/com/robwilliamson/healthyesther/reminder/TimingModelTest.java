/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.reminder;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import com.robwilliamson.healthyesther.reminder.TimingModel;
import com.robwilliamson.healthyesther.util.time.Range;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.robwilliamson.healthyesther.unit.Assert.assertIsEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class TimingModelTest {
    private static final ZonedDateTime MORNING_8AM_21 = ZonedDateTime.of(
            2015,
            3,
            21,
            8,
            0,
            0,
            0,
            ZoneOffset.UTC);
    private static final ZonedDateTime MORNING_21 = ZonedDateTime.of(
            2015,
            3,
            21,
            7,
            0,
            0,
            0,
            ZoneId.systemDefault());
    private static final ZonedDateTime MIDDAY_21 = ZonedDateTime.of(
            2015,
            3,
            21,
            12,
            0,
            0,
            0,
            ZoneId.systemDefault());
    private static final ZonedDateTime EVENING_21 = ZonedDateTime.of(
            2015,
            3,
            21,
            22,
            0,
            0,
            0,
            ZoneId.systemDefault());
    private static final ZonedDateTime MIDNIGHT_21 = ZonedDateTime.of(
            2015,
            3,
            21,
            0,
            0,
            0,
            0,
            ZoneId.systemDefault());
    private static final Range ALLOWED = new Range(MORNING_21, EVENING_21);
    private static final Duration PERIOD = Duration.ofHours(1);
    private static final Duration HALF_PERIOD = Duration.ofMinutes(30);
    private static final Duration MIN_NOTIFICATION_SEPARATION = Duration.ofMinutes(30);

    private MockContext mContext;
    private MockTimingModelEnvironment mEnvironment;
    private TimingModel mSubject;

    @SuppressWarnings("RedundantThrows")
    @Before
    public void setUp() throws Exception {
        mContext = new MockContext();
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
        mSubject.onAlarmElapsed(mContext);
        assertIsEqual(MORNING_21, mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
    }

    @Test
    public void testOnAlarmElapsed_inAllowedRange() {
        mEnvironment.now = MORNING_21;
        mSubject.onAlarmElapsed(mContext);
        assertIsEqual(MORNING_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(1, mEnvironment.sendReminderCallCount);
    }

    @Test
    public void testOnAlarmElapsed_doesNotChangeNextAfterPrematureElapsed() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.plus(HALF_PERIOD);
        mEnvironment.lastNotifiedTime = MIDDAY_21.minus(HALF_PERIOD);
        mSubject.onAlarmElapsed(mContext);
        assertIsEqual(MIDDAY_21.plus(HALF_PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
        assertNull(mEnvironment.setLastNotifiedTimeParams);
    }

    @Test
    public void testOnAlarmElapsed_isIdempotent() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.plus(HALF_PERIOD);
        mEnvironment.lastNotifiedTime = MIDDAY_21.minus(HALF_PERIOD);
        mSubject.onAlarmElapsed(mContext);

        assertIsEqual(MIDDAY_21.plus(HALF_PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
        assertNull(mEnvironment.setLastNotifiedTimeParams);

        mEnvironment.now = MIDDAY_21.plus(Duration.ofMinutes(1));
        mSubject.onAlarmElapsed(mContext);
        mSubject.onAlarmElapsed(mContext);

        assertIsEqual(MIDDAY_21.plus(HALF_PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
        assertNull(mEnvironment.setLastNotifiedTimeParams);
    }

    @Test
    public void testEnsureNotificationIsPending_nextIsSet() throws Exception {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.nextNotificationTime = MIDDAY_21.plus(Duration.ofMinutes(1));
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
        mSubject.onApplicationCreated(mContext);

        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnBootCompleted() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.setLastNotifiedTime(MORNING_21.minus(Duration.ofDays(1)), mContext);
        mSubject.onBootCompleted(mContext);

        assertEquals(1, mEnvironment.sendReminderCallCount);
        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnNotified() {
        mSubject.onNotified(mContext);
        assertIsEqual(MORNING_8AM_21, Duration.ofSeconds(1), mEnvironment.setLastNotifiedTimeParams.time);
        assertIsEqual(MORNING_8AM_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnScreenOn() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.setLastNotifiedTime(MORNING_21.minus(Duration.ofDays(1)), mContext);
        mSubject.onScreenOn(mContext);

        assertEquals(1, mEnvironment.sendReminderCallCount);
        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
    }

    @Test
    public void testOnUserEntry() {
        mEnvironment.now = MIDDAY_21;
        mEnvironment.setLastNotifiedTime(MIDDAY_21.minus(HALF_PERIOD), mContext);
        mEnvironment.appInForeground = true;
        mSubject.onUserEntry(mContext);
        assertIsEqual(MIDDAY_21.plus(PERIOD), mEnvironment.setAlarmParams.alarmTime);
        assertEquals(0, mEnvironment.sendReminderCallCount);
    }

    private void ensureNotificationIsPending(TimingModel model) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = TimingModel.class.getDeclaredMethod(
                "ensureNotificationIsPending",
                Context.class
        );
        method.setAccessible(true);
        method.invoke(model, mContext);
    }

    private static class MockTimingModelEnvironment implements TimingModel.Environment {
        public ZonedDateTime now = MORNING_8AM_21;
        boolean appInForeground = false;
        ZonedDateTime lastNotifiedTime = null;
        ZonedDateTime nextNotificationTime = null;

        SetLastNotifiedTimeParams setLastNotifiedTimeParams = null;
        SetNextNotificationTimeParams setNextNotificationTimeParams = null;
        SetAlarmParams setAlarmParams = null;
        int sendReminderCallCount = 0;

        @Override
        public ZonedDateTime getNow() {
            return now;
        }

        @Override
        public ZonedDateTime getLastNotifiedTime(Context context) {
            if (lastNotifiedTime != null) {
                return lastNotifiedTime;
            }

            return setLastNotifiedTimeParams == null ? null : setLastNotifiedTimeParams.time;
        }

        @Override
        public void setLastNotifiedTime(final ZonedDateTime time, Context context) {
            setLastNotifiedTimeParams = new SetLastNotifiedTimeParams();
            setLastNotifiedTimeParams.time = time;
        }

        @Override
        public ZonedDateTime getNextNotificationTime(Context context) {
            if (nextNotificationTime != null) {
                return nextNotificationTime;
            }

            return setNextNotificationTimeParams == null ? null : setNextNotificationTimeParams.alarmTime;
        }

        @Override
        public void setNextNotificationTime(ZonedDateTime time, Context context) {
            setNextNotificationTimeParams = new SetNextNotificationTimeParams();
            setNextNotificationTimeParams.alarmTime = time;
        }

        @Override
        public boolean appInForeground() {
            return appInForeground;
        }

        @Override
        public void setAlarm(ZonedDateTime alarmTime, Context context) {
            setAlarmParams = new SetAlarmParams();
            setAlarmParams.alarmTime = alarmTime;
        }

        @Override
        public void sendReminder(Context context) {
            sendReminderCallCount++;
        }

        public static class SetLastNotifiedTimeParams {
            public ZonedDateTime time;
        }

        public static class SetNextNotificationTimeParams {
            ZonedDateTime alarmTime;
        }

        public static class SetAlarmParams {
            ZonedDateTime alarmTime;
        }
    }
}
