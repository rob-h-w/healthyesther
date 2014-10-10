package com.robwilliamson.healthyesther;

import android.support.v4.app.Fragment;

import com.robwilliamson.healthyesther.fragment.BusyFragment;

public class BusyActivity extends BaseFragmentActivity {
    private static final String BUSY_TAG = "Busy";
    private volatile int mBusy = 0;

    protected synchronized void setBusy(boolean busy) {
        boolean startBusyFragment = false;
        boolean stopBusyFragment = false;

        if (busy) {
            mBusy++;
        } else {
            if (mBusy > 0) {
                mBusy--;
            }
        }

        if (!isStateLoaded()) {
            return;
        }

        boolean busyRunning = getBusyFragment() != null;

        if (!busyRunning && mBusy > 0) {
            startBusyFragment = true;
        }

        if (busyRunning && mBusy == 0) {
            stopBusyFragment = true;
        }

        if (startBusyFragment) {
            Fragment busyFragment = new BusyFragment();
            busyFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, busyFragment, BUSY_TAG).commit();
        }

        if (stopBusyFragment) {
            getSupportFragmentManager().beginTransaction().remove(getBusyFragment()).commit();
        }
    }

    private Fragment getBusyFragment() {
        return getSupportFragmentManager().findFragmentByTag(BUSY_TAG);
    }
}
