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
        public void setNextNotificationTime(DateTime time);
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

    public void onAlarmElapsed() {
        notifyIfAppropriate();
        ensureNotificationIsPending();
    }

    public void onApplicationCreated() {
        ensureNotificationIsPending();
    }

    public void onBootCompleted() {
        notifyIfAppropriate();
        ensureNotificationIsPending();
    }

    public void onNotified() {
        DateTime now = mEnvironment.getNow();
        mEnvironment.setLastNotifiedTime(now);
        mEnvironment.setNextNotificationTime(null);
        setAlarm(getNextNotificationAfter(now));
    }

    public void onScreenOn() {
        notifyIfAppropriate();
        ensureNotificationIsPending();
    }

    public void onUserEntry() {
        setAlarm(getNextNotificationAfter(mEnvironment.getNow()));
    }

    private boolean shouldNotify() {
        DateTime now = mEnvironment.getNow();
        boolean notificationAllowed = allowedTimes().contains(now) &&
                !mEnvironment.appInForeground();

        if (!notificationAllowed) {
            return false;
        }

        if (hasNotifiedBefore()) {
            if (coolOffPeriod().contains(now)) {
                return false;
            }

            DateTime next = mEnvironment.getNextNotificationTime();
            if (next == null ||
                    new Range(now, SIGMA).contains(next) ||
                    next.isBefore(now)) {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    private void notifyIfAppropriate() {
        if (shouldNotify()) {
            mEnvironment.sendReminder();
        }
    }

    private void ensureNotificationIsPending() {
        DateTime now = mEnvironment.getNow();
        DateTime next = mEnvironment.getNextNotificationTime();

        if (next == null
                || next.isBefore(now)
                || next.minus(mPeriod).isAfter(now)) {
            setAlarm(getNextNotificationAfter(now));
        }
    }

    private DateTime getNextNotificationAfter(DateTime before) {
        if (before == null) {
            before = mEnvironment.getNow();
        }

        DateTime next = before.plus(mPeriod);

        if (allowedTimes().contains(next)) {
            return next;
        }

        next = allowedTimes().getEdgeAfter(next);

        if (next == null) {
            next = allowedTimes().to;
        }

        return next;
    }

    private boolean hasNotifiedBefore() {
        return mEnvironment.getLastNotifiedTime() != null;
    }

    private Range coolOffPeriod() {
        return new Range(mEnvironment.getLastNotifiedTime(), mMinTimeBetweenNotifications);
    }

    private RangeSet allowedTimes() {
        DateTime yesterday = mEnvironment.getNow().minus(Duration.standardDays(1));
        return mAllowedNotificationTimes.startingFrom(yesterday.getYear(), yesterday.getMonthOfYear(), yesterday.getDayOfMonth());
    }

    private void setAlarm(DateTime alarmTime) {
        mEnvironment.setAlarm(alarmTime);
        mEnvironment.setNextNotificationTime(alarmTime);
    }
}
