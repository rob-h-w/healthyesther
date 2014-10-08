package com.robwilliamson.healthyesther;

import android.os.Bundle;

import com.robwilliamson.healthyesther.fragment.BusyFragment;

public class BusyActivity extends BaseFragmentActivity {
    private boolean mBusyIsSettable = false;
    private BusyFragment mBusyFragment;

    public BusyActivity() {
        mBusyFragment = new BusyFragment();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mBusyIsSettable = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBusyIsSettable = false;
    }

    protected synchronized void setBusy(boolean busy) {
        if (!mBusyIsSettable) {
            return;
        }

        if (busy) {
            mBusyFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, mBusyFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(mBusyFragment).commit();
        }
    }
}
