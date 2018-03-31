/*
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
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
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.util.TimeZone;

public enum TimingManager {
    INSTANCE;

    private static final Duration PERIOD = Duration.standardHours(1);
    private static final Duration MULTIPLE_NOTIFICATION_THRESHOLD = Duration.standardMinutes(50);
    private static final Range ALLOWED_NOTIFICATION_RANGE = new Range(Utils.Time.localNow().withTime(7, 0, 0, 0),
            Utils.Time.localNow().withTime(22, 0, 0, 0));

    private static final String PREFERENCES_NAME =
            "com.robwilliamson.healthyesther.reminder.TimingManager";
    private static final String NEXT_REMINDER = "next_reminder";
    private static final String PREVIOUS_REMINDER = "previous_reminder";

    private static final String LOG_TAG = TimingManager.class.getSimpleName();

    private static final long BIP = 50;
    private static final long GAP = 150;
    private static final long MINI_GAP = 75;
    private Environment mModelEnvironment = new Environment();
    private volatile TimingModel mTimingModel = null;

    private static DateTime makeLocal(DateTime time) {
        if (time == null) {
            return null;
        }

        return time.withZone(DateTimeZone.forTimeZone(TimeZone.getDefault()));
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

    public void applicationCreated(Context context) {
        log("applicationCreated");
        getTimingModel().onApplicationCreated(context);
    }

    public void onAlarmElapsed(Context context) {
        getTimingModel().onAlarmElapsed(context);
    }

    public void onBootCompleted(Context context) {
        getTimingModel().onBootCompleted(context);
    }

    public void onScreenOn(Context context) {
        getTimingModel().onScreenOn(context);
    }

    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private PendingIntent getOperation(Context context) {
        Intent intent = new Intent(context, ReminderIntentService.class);

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private DateTime getTime(String key, Context context) {
        String timeString = getPreferences(context).getString(key, null);

        if (timeString == null) {
            return null;
        }

        try {
            return Utils.Time.fromLocalString(timeString);
        } catch (IllegalArgumentException exception) {
            log("Couldn't get time from " + timeString, exception);

            // Remove the invalid value.
            setTime(key, null, context);
            return null;
        }
    }

    private void setTime(String key, DateTime time, Context context) {
        if (time == null) {
            getPreferences(context).edit().remove(key).apply();
        } else {
            getPreferences(context).edit().putString(key, Utils.Time.toLocalString(time)).apply();
        }
    }

    private synchronized TimingModel getTimingModel() {
        if (mTimingModel == null) {
            mTimingModel = new TimingModel(mModelEnvironment, PERIOD, MULTIPLE_NOTIFICATION_THRESHOLD, ALLOWED_NOTIFICATION_RANGE);
        }

        return mTimingModel;
    }

    private class Environment implements TimingModel.Environment {

        @Override
        public DateTime getNow() {
            DateTime now = Utils.Time.localNow();
            log("getNow returning " + now);
            return now;
        }

        @Override
        public DateTime getLastNotifiedTime(Context context) {
            DateTime lastNotified = makeLocal(getTime(PREVIOUS_REMINDER, context));
            log("getLastNotifiedTime " + lastNotified);
            return lastNotified;
        }

        @Override
        public void setLastNotifiedTime(DateTime time, Context context) {
            time = makeLocal(time);
            setTime(PREVIOUS_REMINDER, time, context);
            log("setLastNotifiedTime " + time);
        }

        @Override
        public DateTime getNextNotificationTime(Context context) {
            DateTime nextNotificationTime = makeLocal(getTime(NEXT_REMINDER, context));
            log("getNextNotificationTime" + nextNotificationTime);
            return nextNotificationTime;
        }

        @Override
        public void setNextNotificationTime(DateTime time, Context context) {
            time = makeLocal(time);
            log("setNextNotificationTime(" + time + ")");
            setTime(NEXT_REMINDER, time, context);
        }

        @Override
        public boolean appInForeground() {
            return App.getInForeGround();
        }

        @Override
        public void setAlarm(DateTime alarmTime, Context context) {
            alarmTime = makeLocal(alarmTime);
            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (BuildConfig.DEBUG) {
                long millisUntil = alarmTime.getMillis() - getNow().getMillis();

                log("setting new notification in " + millisUntil + "ms, expected to trigger at " + alarmTime);
            }

            // Use time elapsed since boot.
            assert alarmManager != null;
            alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME,
                    Utils.Time.toBootRealTimeElapsedMillis(alarmTime),
                    getOperation(context)
            );
        }

        @Override
        public void sendReminder(Context context) {
            Uri soundToBugEsther = Uri.parse("android.resource://com.robwilliamson.healthyesther/" + R.raw.hello);
            Intent intent = new Intent(context, HomeActivity.class);
            PendingIntent reminderPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.reminder_content_title))
                    .setContentText(context.getString(R.string.reminder_content))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(reminderPendingIntent)
                    .setTicker(context.getString(R.string.reminder_ticker))
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
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            assert notificationManager != null;
            notificationManager.notify(1, notification);

            mTimingModel.onNotified(context);
            log("Reminder sent");
        }
    }
}
