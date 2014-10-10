package com.robwilliamson.healthyesther;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

public class BaseFragmentActivity extends FragmentActivity {
    private volatile boolean mActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(getContentLayoutResourceId());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mActive = true;
    }

    protected boolean isActive() {
        return mActive;
    }

    protected int getContentLayoutResourceId() {
        return R.layout.activity_base_root;
    }

    protected int getActivityContentLayoutResourceId() {
        return R.id.base_activity_content_layout;
    }

    protected LinearLayout getActivityContentLayout() {
        return (LinearLayout)findViewById(getActivityContentLayoutResourceId());
    }
}
