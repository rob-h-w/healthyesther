package com.robwilliamson.healthyesther.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.robwilliamson.healthyesther.App;
import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public enum TimingManager {
    INSTANCE;

    private static final Duration PERIOD = Duration.standardHours(1);
    private static final Duration MULTIPLE_NOTIFICATION_THRESHOLD = Duration.standardMinutes(50);
    private static final Range ALLOWED_NOTIFICATION_RANGE = new Range(DateTime.now().withTime(7, 0, 0, 0),
            DateTime.now().withTime(22, 0, 0, 0));

    private static final String PREFERENCES_NAME =
            "com.robwilliamson.healthyesther.reminder.TimingManager";
    private static final String NEXT_REMINDER = "next_reminder";
    private static final String PREVIOUS_REMINDER = "previous_reminder";

    private static final String LOG_TAG = TimingManager.class.getSimpleName();

    private static final long BIP = 75;
    private static final long GAP = 150;
    private static final long MINI_GAP = 75;

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
        public boolean appInForeground() {
            return App.getInForeGround();
        }

        @Override
        public void setAlarm(DateTime alarmTime) {
            AlarmManager alarmManager =
                    (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            long millisUntil = alarmTime.getMillis() - getNow().getMillis();

            log("setting new notification in " + millisUntil + "ms, expected to trigger at " + alarmTime);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME, millisUntil, getOperation());

            setTime(NEXT_REMINDER, alarmTime);

        }

        @Override
        public void sendReminder() {
            Uri soundToBugEsther = Uri.parse("android.resource://com.robwilliamson.healthyesther/" + R.raw.hello );
            Intent intent = new Intent(getContext(), HomeActivity.class);
            PendingIntent reminderPendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(getContext())
                    .setContentTitle(getContext().getString(R.string.reminder_content_title))
                    .setContentText(getContext().getString(R.string.reminder_content))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(reminderPendingIntent)
                    .setTicker(getContext().getString(R.string.reminder_ticker))
                    .setAutoCancel(true)
                    .setVibrate(new long[]{
                            0, BIP, GAP, BIP, GAP, BIP, GAP, BIP,
                            MINI_GAP, BIP, MINI_GAP, BIP,
                            GAP, BIP, GAP, BIP
                    })
                    .setStyle(new Notification.InboxStyle())
                    .setSound(soundToBugEsther)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, notification);

            mTimingModel.onNotified();
            log("Reminder sent");
        }
    }

    private Context mContext;
    private Environment mModelEnvironment = new Environment();
    private volatile TimingModel mTimingModel = null;

    public void applicationCreated(Context context) {
        log("applicationCreated");
        setContext(context);
        getTimingModel().onApplicationCreated();
    }

    public void onAlarmElapsed(Context context) {
        setContext(context);
        getTimingModel().onAlarmElapsed();
    }

    public void onBootCompleted(Context context) {
        setContext(context);
        getTimingModel().onBootCompleted();
    }

    public void onScreenOn(Context context) {
        setContext(context);
        getTimingModel().onScreenOn();
    }

    private SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private PendingIntent getOperation() {
        Intent intent = new Intent(getContext(), ReminderIntentService.class);

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

        try {
            return Utils.Time.fromLocalString(timeString);
        } catch (IllegalArgumentException exception) {
            log("Couldn't get time from " + timeString, exception);

            // Remove the invalid value.
            setTime(key, null);
            return null;
        }
    }

    private void setTime(String key, DateTime time) {
        if (time == null) {
            getPreferences().edit().remove(key).apply();
        } else {
            getPreferences().edit().putString(key, Utils.Time.toLocalString(time)).apply();
        }
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

    private static void log(String message, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message, e);
        }
    }
}
