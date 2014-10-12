package com.robwilliamson.healthyesther;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.robwilliamson.healthyesther.fragment.BusyFragment;

import java.util.concurrent.CountDownLatch;

public class BusyActivity extends BaseFragmentActivity {
    private static final String BUSY_TAG = "Busy";
    private volatile int mBusy = 0;

    @Override
    protected void onResume() {
        super.onResume();

        updateUi();
    }

    protected void setBusy(final boolean busy) {
        final CountDownLatch latch = new CountDownLatch(1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doLatchProtectedWork();

                latch.countDown();
            }

            private void doLatchProtectedWork() {
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

                updateUi();
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
        return mBusy > 0;
    }

    private Fragment getBusyFragment() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager == null) {
            return null;
        }

        return manager.findFragmentByTag(BUSY_TAG);
    }

    private boolean hasBusyFragment() {
        return getBusyFragment() != null;
    }

    private void updateUi() {
        Utils.View.assertIsOnUiThread();
        FragmentManager manager = getSupportFragmentManager();
        boolean hasBusyFragment = hasBusyFragment();

        if (isBusy()) {
            if (hasBusyFragment) {
                return;
            }

            Fragment busyFragment = new BusyFragment();
            busyFragment.setArguments(getIntent().getExtras());
            manager.beginTransaction().add(android.R.id.content, busyFragment, BUSY_TAG).commit();

            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else {
            if (!hasBusyFragment) {
                return;
            }

            getSupportFragmentManager().beginTransaction().remove(getBusyFragment()).commit();

            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }

        invalidateOptionsMenu();
    }
}
