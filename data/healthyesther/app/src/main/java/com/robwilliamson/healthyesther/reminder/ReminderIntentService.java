package com.robwilliamson.healthyesther.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ReminderIntentService extends IntentService {
    private static final String CLASS_NAME = ReminderIntentService.class.getSimpleName();
    private static final String LOG_TAG = CLASS_NAME;

    public ReminderIntentService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            TimingManager.INSTANCE.alarmElapsed(getBaseContext());

            Intent reminderIntent = new Intent(getBaseContext(), HomeActivity.class);
            PendingIntent reminderPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(getBaseContext())
                    .setContentTitle(getBaseContext().getString(R.string.reminder_content_title))
                    .setContentText(getBaseContext().getString(R.string.reminder_content))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(reminderPendingIntent)
                    .setTicker(getBaseContext().getString(R.string.reminder_ticker))
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, notification);
            Log.d(LOG_TAG, "notification made");
        }
    }
}
