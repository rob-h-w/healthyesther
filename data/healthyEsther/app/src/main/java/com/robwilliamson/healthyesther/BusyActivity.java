package com.robwilliamson.healthyesther;

import android.os.Bundle;

import com.robwilliamson.healthyesther.fragment.BusyFragment;

public class BusyActivity extends BaseFragmentActivity {
    private BusyFragment mBusyFragment;

    public BusyActivity() {
        mBusyFragment = new BusyFragment();
    }

    protected synchronized void setBusy(boolean busy) {
        if (!isStateLoaded()) {
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
