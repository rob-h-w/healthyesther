/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.reminder.TimingManager;

import java.util.TimeZone;

import javax.annotation.Nullable;

public class App extends Application {
    private static App sInstance = null;
    private static long sUiThreadId;
    private static volatile boolean sInForeground = false;
    private static volatile String sName = null;

    public static long getUiThreadId() {
        return sUiThreadId;
    }

    public static void setsUiThreadId(long id) {
        sUiThreadId = id;
    }

    public static App getInstance() {
        return sInstance;
    }

    public static synchronized void setInForeground(boolean inForeground) {
        sInForeground = inForeground;
    }

    public static synchronized boolean getInForeGround() {
        return sInForeground;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            HealthDbHelper.sDebug = true;
        }

        sUiThreadId = Thread.currentThread().getId();
        sInstance = this;

        TimingManager.INSTANCE.applicationCreated(getApplicationContext());
    }

    @Nullable
    public synchronized String getUsername() {
        if (sName != null) {
            return sName;
        }

        AccountManager mgr = AccountManager.get(this);

        Account[] accounts = mgr.getAccounts();

        for (Account account : accounts) {
            if (account.type.equals("com.google")) {
                sName = account.name;
                break;
            }
        }

        return sName;
    }
}
