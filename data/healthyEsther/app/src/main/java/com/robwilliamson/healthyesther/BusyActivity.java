package com.robwilliamson.healthyesther;

import android.support.v4.app.FragmentActivity;

import com.robwilliamson.healthyesther.fragment.BusyFragment;

public class BusyActivity extends FragmentActivity {
    private BusyFragment mBusyFragment;

    public BusyActivity() {
        mBusyFragment = new BusyFragment();
    }

    protected synchronized void setBusy(boolean busy) {
        if (busy) {
            mBusyFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, mBusyFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(mBusyFragment).commit();
        }
    }
}
