package com.robwilliamson.healthyesther.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;

public enum TimingManager {
    INSTANCE;

    private static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;

    public void next(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent nextAlarmPendingIntent = getOperation(context);
        alarmManager.cancel(nextAlarmPendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, getMillis(), nextAlarmPendingIntent);
    }

    private PendingIntent getOperation(Context context) {
        Intent intent = new Intent(context, ReminderIntentService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private long getMillis() {
        DateTime now = DateTime.now().withZone(DateTimeZone.getDefault());
        DateTime earliest = DateTime.now().withHourOfDay(8).withMinuteOfHour(0);
        DateTime latest = DateTime.now().withHourOfDay(22).withMinuteOfHour(0);
        long period = 2 * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;

        long next = period;

        if (now.isAfter(latest)) {
            // Move earliest until tomorrow.
            earliest = earliest.plus(Period.days(1));
        }

        if (now.isBefore(earliest) || now.isAfter(latest)) {
            Interval waitInterval = new Interval(now, earliest);
            period = waitInterval.getEndMillis();
        }

        return next;
    }
}
