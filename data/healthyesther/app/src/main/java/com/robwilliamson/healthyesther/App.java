package com.robwilliamson.healthyesther;

import android.app.Application;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.reminder.TimingManager;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTimeZone;

import java.util.TimeZone;

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


}
