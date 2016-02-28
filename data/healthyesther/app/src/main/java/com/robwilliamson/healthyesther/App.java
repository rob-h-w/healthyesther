package com.robwilliamson.healthyesther;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.reminder.TimingManager;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTimeZone;

import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Nullable;

public class App extends Application {
    private static App sInstance = null;
    private static long sUiThreadId;
    private static volatile boolean sInForeground = false;

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

        JodaTimeAndroid.init(this);
        DateTimeZone.setDefault(DateTimeZone.forOffsetMillis(TimeZone.getDefault().getRawOffset()));

        if (BuildConfig.DEBUG) {
            HealthDbHelper.sDebug = true;
        }

        sUiThreadId = Thread.currentThread().getId();
        sInstance = this;

        TimingManager.INSTANCE.applicationCreated(getApplicationContext());
    }

    @Nullable
    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }
}
