package com.robwilliamson.healthyesther.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ReminderIntentService extends IntentService {
    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Intent reminderIntent = new Intent(getBaseContext(), HomeActivity.class);
            PendingIntent reminderPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(getBaseContext())
                    .setContentTitle("Wassup?")
                    .setContentText("Content text.")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(reminderPendingIntent)
                    .setTicker("ReminderIntentService finished.")
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, notification);

            TimingManager.INSTANCE.next(getBaseContext());
        }
    }
}
