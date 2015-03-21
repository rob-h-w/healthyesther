package com.robwilliamson.healthyesther;

import android.app.Application;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.healthyesther.reminder.TimingManager;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class App extends Application {
    private static App sInstance = null;
    private static long sUiThreadId;

    public static long getUiThreadId() {
        return sUiThreadId;
    }

    public static void setsUiThreadId(long id) {
        sUiThreadId = id;
    }

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
        DateTimeZone.setDefault(DateTimeZone.forTimeZone(TimeZone.getDefault()));

        if (BuildConfig.DEBUG) {
            HealthDbHelper.sDebug = true;
        }

        sUiThreadId = Thread.currentThread().getId();
        sInstance = this;

        TimingManager.INSTANCE.applicationCreated(getApplicationContext());
    }


}
