package com.robwilliamson.healthyesther;

import android.app.Application;

import com.robwilliamson.db.HealthDbHelper;

import net.danlew.android.joda.JodaTimeAndroid;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        if (BuildConfig.DEBUG) {
            HealthDbHelper.sDebug = true;
        }
    }
}
