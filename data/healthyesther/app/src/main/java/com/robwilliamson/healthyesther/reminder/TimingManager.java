package com.robwilliamson.healthyesther.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public enum TimingManager {
    INSTANCE;

    private static final Duration PERIOD = Duration.standardSeconds(10);
    private static final Duration MULTIPLE_NOTIFICATION_THRESHOLD = Duration.standardMinutes(1);
    private static final Range ALLOWED_NOTIFICATION_RANGE = new Range(DateTime.now().withTime(7, 0, 0, 0),
            DateTime.now().withTime(22, 0, 0, 0));

    private static final String PREFERENCES_NAME =
            "com.robwilliamson.healthyesther.reminder.TimingManager";
    private static final String NEXT_REMINDER = "next_reminder";
    private static final String PREVIOUS_REMINDER = "previous_reminder";
    private static final String REQUEST_CODE = "request_code";

    private static final String LOG_TAG = TimingManager.class.getSimpleName();

    private class Environment implements TimingModel.Environment {

        @Override
        public DateTime getNow() {
            return DateTime.now();
        }

        @Override
        public void setLastNotifiedTime(DateTime time) {
            setTime(PREVIOUS_REMINDER, time);
            log("setLastNotifiedTime " + time);
        }

        @Override
        public DateTime getLastNotifiedTime() {
            return getTime(PREVIOUS_REMINDER);
        }

        @Override
        public DateTime getNextNotificationTime() {
            return getTime(NEXT_REMINDER);
        }

        @Override
        public boolean appInForeground() {
            return false;
        }

        @Override
        public void setAlarm(DateTime alarmTime) {
            setTime(NEXT_REMINDER, alarmTime);
        }

        @Override
        public void sendReminder() {
            PendingIntent reminderPendingIntent = getOperation();
            Notification notification = new Notification.Builder(getContext())
                    .setContentTitle(getContext().getString(R.string.reminder_content_title))
                    .setContentText(getContext().getString(R.string.reminder_content))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(reminderPendingIntent)
                    .setTicker(getContext().getString(R.string.reminder_ticker))
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, notification);

            mTimingModel.onNotified();
        }
    }

    private Context mContext;
    private Environment mModelEnvironment = new Environment();
    private TimingModel mTimingModel = null;

    public void applicationCreated(Context context) {
        log("applicationCreated");
        setContext(context);
        getTimingModel().onApplicationCreated();
    }

    public void alarmElapsed(Context context, Intent intent) {
        setContext(context);
        log("alarmElapsed");
        long alarmId = getAlarmId();
        boolean useAlarm = alarmId == -1 ||
                alarmId == intent.getLongExtra(REQUEST_CODE, -1L);

        if (useAlarm) {
            getTimingModel().onAlarmExpired();
        }
    }

    private SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private PendingIntent getOperation() {
        Intent intent = new Intent(getContext(), ReminderIntentService.class);

        long alarmId = getAlarmId();

        intent.putExtra(REQUEST_CODE, alarmId++);
        setAlarmId(alarmId);

        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    private DateTime getTime(String key) {
        String timeString = getPreferences().getString(key, null);

        if (timeString == null) {
            return null;
        }

        return Utils.Time.fromLocalString(timeString);
    }

    private void setTime(String key, DateTime time) {
        if (time == null) {
            getPreferences().edit().remove(key).apply();
        } else {
            getPreferences().edit().putString(key, Utils.Time.toLocalString(time)).apply();
        }
    }

    private long getAlarmId() {
        return getPreferences().getLong(REQUEST_CODE, -1);
    }

    private void setAlarmId(long id) {
        getPreferences().edit().putLong(REQUEST_CODE, id).apply();
    }

    private synchronized TimingModel getTimingModel() {
        if (mTimingModel == null) {
            mTimingModel = new TimingModel(mModelEnvironment, PERIOD, MULTIPLE_NOTIFICATION_THRESHOLD, ALLOWED_NOTIFICATION_RANGE);
        }

        return mTimingModel;
    }

    private static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message);
        }
    }
}
