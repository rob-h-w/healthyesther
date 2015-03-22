package com.robwilliamson.healthyesther.reminder;

import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class TimingModel {
    private static final Duration SIGMA = Duration.standardMinutes(1);
    private final Environment mEnvironment;
    private final Duration mMinTimeBetweenNotifications;
    private final Range mDisallowedNotificationTimes;
    private final Duration mPeriod;

    public static interface Environment {
        public void setLastNotifiedTime(DateTime time);
        public DateTime getLastNotifiedTime();
        public DateTime getNextNotificationTime();
        public boolean appInForeground();
        public void setAlarm(DateTime alarmTime);
    }

    public TimingModel(Environment environment,
                       Duration period,
                       Duration minTimeBetweenNotifications,
                       Range disallowedNotificationTimes) {
        mEnvironment = environment;
        mPeriod = period;
        mMinTimeBetweenNotifications = minTimeBetweenNotifications;
        mDisallowedNotificationTimes = disallowedNotificationTimes;
    }

    public void onAlarmExpired() {
        if (shouldSkipNotification()) {
            mEnvironment.setAlarm(getNextNotificationAfter(mEnvironment.getNextNotificationTime()));
        }
    }

    public void onNotified() {
        mEnvironment.setLastNotifiedTime(DateTime.now());
    }

    public boolean shouldNotify() {
        if (hasNotifiedBefore()) {
            if (cooloffPeriod().contains(DateTime.now())) {
                return false;
            }
        }

        return !mEnvironment.appInForeground() &&
                (!hasNotifiedBefore() ||
                        now().contains(mEnvironment.getNextNotificationTime()));
    }

    private DateTime getNextNotificationAfter(DateTime before) {
        if (before == null) {
            before = DateTime.now();
        }

        DateTime next = before.plus(mPeriod);

        if (disallowed(next).contains(next, Range.Comparison.EXCLUSIVE)) {
            next = disallowed(next).to;
        }

        return next;
    }

    private boolean shouldSkipNotification() {
        return !shouldNotify();
    }

    private boolean hasNotifiedBefore() {
        return mEnvironment.getLastNotifiedTime() != null;
    }

    private boolean hasSetAlarmBefore() {
        return mEnvironment.getNextNotificationTime() != null;
    }

    private Range cooloffPeriod() {
        return new Range(mEnvironment.getLastNotifiedTime(), mMinTimeBetweenNotifications);
    }

    private Range now() {
        return new Range(DateTime.now(), SIGMA);
    }

    private Range disallowed(DateTime day) {
        return mDisallowedNotificationTimes.starting(
                mDisallowedNotificationTimes.from.withDate(
                        day.getYear(), day.getMonthOfYear(), day.getDayOfMonth()));
    }
}
