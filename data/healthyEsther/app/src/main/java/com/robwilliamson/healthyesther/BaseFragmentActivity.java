package com.robwilliamson.healthyesther;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseFragmentActivity extends FragmentActivity {
    private class SetContentViewDisallowedException extends RuntimeException {
        SetContentViewDisallowedException(String message) {
            super(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(getContentLayoutResourceId());
    }

    protected int getContentLayoutResourceId() {
        return R.layout.activity_base_root;
    }
}
