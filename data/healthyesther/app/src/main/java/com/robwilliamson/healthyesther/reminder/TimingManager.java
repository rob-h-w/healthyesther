package com.robwilliamson.healthyesther.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public enum TimingManager {
    INSTANCE;

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
        return 5000;
    }
}
