package com.robwilliamson.healthyesther;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.Window;

import java.util.concurrent.CountDownLatch;

public class BusyActivity extends BaseFragmentActivity {
    private static final String BUSY_TAG = "Busy";
    private static volatile Dialog mDialog;
    private volatile int mBusy = 0;

    @Override
    protected void onResume() {
        super.onResume();

        updateUi();
    }

    protected boolean isBusy() {
        return mBusy > 0;
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

    private Fragment getBusyFragment() {
        return getFragment(BUSY_TAG);
    }

    private boolean hasBusyFragment() {
        return getBusyFragment() != null;
    }

    private void updateUi() {
        Utils.View.assertIsOnUiThread();
        boolean dialogNotNull = mDialog != null;

        if (isBusy()) {
            if (dialogNotNull) {
                return;
            }

            addDialog();
        } else {
            if (!dialogNotNull) {
                return;
            }

            mDialog.dismiss();
            mDialog = null;
        }

        invalidateOptionsMenu();
    }

    private void addDialog() {
        mDialog = new Dialog(this);

        // Making sure there's no title.
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Dialog cannot be cancelled.
        mDialog.setCancelable(false);

        // Setting the content using prepared XML layout file.
        mDialog.setContentView(R.layout.dialog_busy);
        mDialog.show();
    }
}
