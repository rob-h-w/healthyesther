package com.robwilliamson.healthyesther.reminder;

import com.robwilliamson.healthyesther.util.time.Range;
import com.robwilliamson.healthyesther.util.time.RangeSet;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class TimingModel {
    private static final Duration SIGMA = Duration.standardMinutes(1);
    private final Environment mEnvironment;
    private final Duration mMinTimeBetweenNotifications;
    private final RangeSet mAllowedNotificationTimes;
    private final Duration mPeriod;

    public static interface Environment {
        public DateTime getNow();
        public void setLastNotifiedTime(DateTime time);
        public DateTime getLastNotifiedTime();
        public DateTime getNextNotificationTime();
        public boolean appInForeground();
        public void setAlarm(DateTime alarmTime);
        public void sendReminder();
    }

    public TimingModel(Environment environment,
                       Duration period,
                       Duration minTimeBetweenNotifications,
                       Range allowedNotificationTimes) {
        mEnvironment = environment;
        mPeriod = period;
        mMinTimeBetweenNotifications = minTimeBetweenNotifications;
        mAllowedNotificationTimes = new RangeSet(
                allowedNotificationTimes.startingYesterday(),
                allowedNotificationTimes,
                allowedNotificationTimes.startingTomorrow());
    }

    public void onAlarmExpired() {
        if (shouldSkipNotification()) {
            mEnvironment.setAlarm(getNextNotificationAfter(mEnvironment.getNextNotificationTime()));
            return;
        }

        mEnvironment.sendReminder();
        mEnvironment.setAlarm(getNextNotificationAfter(mEnvironment.getNow()));
    }

    public void onNotified() {
        mEnvironment.setLastNotifiedTime(mEnvironment.getNow());
    }

    public boolean shouldNotify() {
        if (hasNotifiedBefore()) {
            if (cooloffPeriod().contains(mEnvironment.getNow())) {
                return false;
            }
        }

        if (allowedTimes().contains(mEnvironment.getNow())) {
            return !mEnvironment.appInForeground();
        }

        return false;
    }

    private DateTime getNextNotificationAfter(DateTime before) {
        if (before == null) {
            before = mEnvironment.getNow();
        }

        DateTime next = before.plus(mPeriod);

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
        return new Range(mEnvironment.getNow(), SIGMA);
    }

    private RangeSet allowedTimes() {
        DateTime yesterday = mEnvironment.getNow().minus(Duration.standardDays(1));
        return mAllowedNotificationTimes.startingFrom(yesterday.getYear(), yesterday.getMonthOfYear(), yesterday.getDayOfMonth());
    }
}
