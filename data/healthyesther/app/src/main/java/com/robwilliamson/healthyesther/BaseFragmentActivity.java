package com.robwilliamson.healthyesther;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import javax.annotation.Nullable;

public class BaseFragmentActivity extends FragmentActivity {
    private volatile boolean mActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(getContentLayoutResourceId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mActive = false;
        App.setInForeground(mActive);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mActive = true;
        App.setInForeground(mActive);
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

    @Nullable
    protected LinearLayout getActivityContentLayout() {
        return (LinearLayout) findViewById(getActivityContentLayoutResourceId());
    }

    @Nullable
    protected <T extends Fragment> T getFragment(String tag, Class<T> type) {
        return Utils.View.getTypeSafeFragment(getSupportFragmentManager(), tag, type);
    }

    @Nullable
    protected Fragment getFragment(String tag) {
        return getFragment(tag, Fragment.class);
    }
}
