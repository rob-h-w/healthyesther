package com.robwilliamson.healthyesther;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.robwilliamson.healthyesther.fragment.BusyFragment;

import java.util.concurrent.CountDownLatch;

public class BusyActivity extends BaseFragmentActivity {
    private static final String BUSY_TAG = "Busy";
    private volatile int mBusy = 0;

    protected void setBusy(final boolean busy) {
        final CountDownLatch latch = new CountDownLatch(1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doLatchProtectedWork();

                latch.countDown();
            }

            private void doLatchProtectedWork() {
                boolean startBusyFragment = false;
                boolean stopBusyFragment = false;

                if (busy) {
                    mBusy++;
                } else {
                    if (isBusy()) {
                        mBusy--;
                    }
                }

                if (!isActive()) {
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
                    getSupportFragmentManager().beginTransaction().add(R.id.base_activity_root_layout, busyFragment, BUSY_TAG).commit();
                }

                if (stopBusyFragment) {
                    getSupportFragmentManager().beginTransaction().remove(getBusyFragment()).commit();
                }

                invalidateOptionsMenu();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected boolean isBusy() {
        return mBusy > 0 || getBusyFragment() != null;
    }

    private Fragment getBusyFragment() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager == null) {
            return null;
        }

        return manager.findFragmentByTag(BUSY_TAG);
    }
}
