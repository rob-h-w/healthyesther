package com.robwilliamson.healthyesther.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.robwilliamson.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;

public enum TimingManager {
    INSTANCE;

    private static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int PERIOD = 2 * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
    private static final int MULTIPLE_NOTIFICATION_THRESHOLD_S = 5;

    private static final String PREFERENCES_NAME =
            "com.robwilliamson.healthyesther.reminder.TimingManager";
    private static final String NEXT_REMINDER = "next_reminder";
    private static final String PREVIOUS_REMINDER = "previous_reminder";

    private static final String LOG_TAG = TimingManager.class.getSimpleName();

    private Context mContext;

    public void applicationCreated(Context context) {
        alarmElapsed(context);
    }

    public void alarmElapsed(Context context) {
        setContext(context);
        String previousReminderString = getPreferences().getString(PREVIOUS_REMINDER, null);
        String nextReminderString = getPreferences().getString(NEXT_REMINDER, null);

        if (previousReminderString != null && nextReminderString != null) {
            // Check we're not getting a cluster of alarms elapsed at once.
            DateTime previousReminder = Utils.Time.fromUtcString(previousReminderString);
            DateTime nextReminder = Utils.Time.fromUtcString(nextReminderString);

            if (nextReminder.minusSeconds(MULTIPLE_NOTIFICATION_THRESHOLD_S).isAfterNow() &&
                    previousReminder.plusSeconds(MULTIPLE_NOTIFICATION_THRESHOLD_S).isBeforeNow()) {
                // Ignore this elapsed event.
                return;
            }
        }

        getPreferences().edit().remove(NEXT_REMINDER).
                putString(PREVIOUS_REMINDER, Utils.Time.toUtcString(DateTime.now())).commit();
        start(context);
    }

    public void start(Context context) {
        setContext(context);
        DateTime nextDateTime = getNextTimeToSet();

        if (nextDateTime != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long millisUntil = nextDateTime.getMillis() - DateTime.now().getMillis();

            Log.d(LOG_TAG, "setting new notification in " + millisUntil + "ms, expected to trigger at " + nextDateTime);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME, millisUntil, getOperation());

            getPreferences().edit().
                    putString(NEXT_REMINDER, Utils.Time.toUtcString(nextDateTime)).
                    putString(PREVIOUS_REMINDER, Utils.Time.toUtcString(DateTime.now())).commit();
        }
    }

    private SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private DateTime getNextTimeToSet() {
        SharedPreferences preferences = getPreferences();
        String lastTime = preferences.getString(NEXT_REMINDER, null);

        if (lastTime != null) {
            Log.d(LOG_TAG, "time read from preferences = " + lastTime);
            DateTime next = Utils.Time.fromUtcString(lastTime);

            if (next.isAfterNow()) {
                Log.d(LOG_TAG, "time is in the future; not setting another alarm.");
                // Already set.
                return null;
            }
        }

        return DateTime.now().plusMillis(getMillis());
    }

    private PendingIntent getOperation() {
        Intent intent = new Intent(getContext(), ReminderIntentService.class);
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private int getMillis() {
        DateTime now = DateTime.now().withZone(DateTimeZone.getDefault());
        DateTime earliest = DateTime.now().withHourOfDay(8).withMinuteOfHour(0);
        DateTime latest = DateTime.now().withHourOfDay(22).withMinuteOfHour(0);

        int next = PERIOD;

        if (now.isAfter(latest)) {
            // Move earliest until tomorrow.
            earliest = earliest.plus(Period.days(1));
        }

        if (now.isBefore(earliest) || now.isAfter(latest)) {
            Interval waitInterval = new Interval(now, earliest);
            next = (int) waitInterval.getEndMillis();
        }

        return next;
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
}
