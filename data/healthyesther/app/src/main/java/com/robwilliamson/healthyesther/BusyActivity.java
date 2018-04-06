/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.Window;

import java.util.concurrent.CountDownLatch;

@SuppressLint("Registered")
public class BusyActivity extends BaseFragmentActivity {
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

    private void updateUi() {
        Utils.View.assertIsOnUiThread();
        boolean dialogIsNull = mDialog == null;

        if (isBusy()) {
            if (!dialogIsNull) {
                return;
            }

            addDialog();
        } else {
            if (dialogIsNull) {
                return;
            }

            try {
                mDialog.dismiss();
            } catch (IllegalArgumentException e) {
                if (!e.getMessage().endsWith("not attached to window manager")) {
                    throw e;
                }
            }
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
