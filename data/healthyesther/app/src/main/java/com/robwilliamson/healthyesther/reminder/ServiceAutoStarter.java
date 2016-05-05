/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceAutoStarter extends BroadcastReceiver {
    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String SCREEN_ON = "android.intent.action.SCREEN_ON";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BOOT_COMPLETED)) {
            TimingManager.INSTANCE.onBootCompleted(context);
        }

        if (action.equals(SCREEN_ON)) {
            TimingManager.INSTANCE.onScreenOn(context);
        }
    }
}
