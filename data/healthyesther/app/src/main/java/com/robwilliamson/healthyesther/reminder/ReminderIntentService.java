package com.robwilliamson.healthyesther.reminder;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ReminderIntentService extends IntentService {
    private static final String CLASS_NAME = ReminderIntentService.class.getSimpleName();

    public ReminderIntentService() {
        super(CLASS_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TimingManager.INSTANCE.onAlarmElapsed(getBaseContext());
    }
}
