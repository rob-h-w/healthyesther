/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.reminder;

import android.content.Context;

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

    public void onAlarmElapsed(Context context) {
        notifyIfAppropriate(context);
        ensureNotificationIsPending(context);
    }

    public void onApplicationCreated(Context context) {
        ensureNotificationIsPending(context);
    }

    public void onBootCompleted(Context context) {
        notifyIfAppropriate(context);
        ensureNotificationIsPending(context);
    }

    public void onNotified(Context context) {
        DateTime now = mEnvironment.getNow();
        mEnvironment.setLastNotifiedTime(now, context);
        mEnvironment.setNextNotificationTime(null, context);
        setAlarm(getNextNotificationAfter(now), context);
    }

    public void onScreenOn(Context context) {
        notifyIfAppropriate(context);
        ensureNotificationIsPending(context);
    }

    public void onUserEntry(Context context) {
        setAlarm(getNextNotificationAfter(mEnvironment.getNow()), context);
    }

    private boolean shouldNotify(Context context) {
        DateTime now = mEnvironment.getNow();
        boolean notificationAllowed = allowedTimes().contains(now) &&
                !mEnvironment.appInForeground();

        if (!notificationAllowed) {
            return false;
        }

        if (hasNotifiedBefore(context)) {
            if (coolOffPeriod(context).contains(now)) {
                return false;
            }

            DateTime next = mEnvironment.getNextNotificationTime(context);
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

    private void notifyIfAppropriate(Context context) {
        if (shouldNotify(context)) {
            mEnvironment.sendReminder(context);
        }
    }

    private void ensureNotificationIsPending(Context context) {
        DateTime now = mEnvironment.getNow();
        DateTime next = mEnvironment.getNextNotificationTime(context);

        if (next == null
                || next.isBefore(now)
                || next.minus(mPeriod).isAfter(now)) {
            setAlarm(getNextNotificationAfter(now), context);
        } else {
            setAlarm(next, context);
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

    private boolean hasNotifiedBefore(Context context) {
        return mEnvironment.getLastNotifiedTime(context) != null;
    }

    private Range coolOffPeriod(Context context) {
        return new Range(mEnvironment.getLastNotifiedTime(context), mMinTimeBetweenNotifications);
    }

    private RangeSet allowedTimes() {
        DateTime yesterday = mEnvironment.getNow().minus(Duration.standardDays(1));
        return mAllowedNotificationTimes.startingFrom(yesterday.getYear(), yesterday.getMonthOfYear(), yesterday.getDayOfMonth());
    }

    private void setAlarm(DateTime alarmTime, Context context) {
        mEnvironment.setAlarm(alarmTime, context);
        mEnvironment.setNextNotificationTime(alarmTime, context);
    }

    public interface Environment {
        DateTime getNow();

        DateTime getLastNotifiedTime(Context context);

        void setLastNotifiedTime(DateTime time, Context context);

        DateTime getNextNotificationTime(Context context);

        void setNextNotificationTime(DateTime time, Context context);

        boolean appInForeground();

        void setAlarm(DateTime alarmTime, Context context);

        void sendReminder(Context context);
    }
}
