package com.robwilliamson.healthyesther;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.edit.MealActivity;
import com.robwilliamson.healthyesther.edit.MedicationActivity;
import com.robwilliamson.healthyesther.edit.NoteActivity;
import com.robwilliamson.healthyesther.edit.ScoreActivity;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.NavigationDrawerFragment;
import com.robwilliamson.healthyesther.fragment.edit.EventListFragment;
import com.robwilliamson.healthyesther.fragment.home.AbstractHomeFragment;


public class HomeActivity extends DbActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        EventListFragment.Watcher {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (findViewById(R.id.drawer_layout) != null) {
            if (savedInstanceState != null) {
                return;
            }

            handleModeChange();
        }
    }

    private void handleModeChange() {
        if (mNavigationDrawerFragment == null) {
            return;
        }

        NavigationDrawerFragment.NavigationDrawerMode mode = mNavigationDrawerFragment.getMode();

        AbstractHomeFragment fragment = mode.getFragment(getSupportFragmentManager());

        if (fragment.getArguments() == null && getIntent().getExtras() != null) {
            fragment.setArguments(getIntent().getExtras());
        }

        mode.replace(R.id.home_activity_content_layout, getSupportFragmentManager());
    }

    @Override
    protected int getContentLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getActivityContentLayoutResourceId() {
        return R.id.home_activity_content_layout;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        handleModeChange();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public QueryUser[] getQueryUsers() {
        return mNavigationDrawerFragment.getMode().getFragment(getSupportFragmentManager()).getQueryUsers();
    }

    @Override
    public void onEventSelected(Event.Value event) {
        Intent intent = null;

        switch (Event.Type.valueOf(event.typeId)) {
            case MEAL:
                intent = new Intent(this, MealActivity.class);
                break;
            case MEDICATION:
                intent = new Intent(this, MedicationActivity.class);
                break;
            case HEALTH_SCORE:
                intent = new Intent(this, ScoreActivity.class);
                break;
            case NOTE:
                intent = new Intent(this, NoteActivity.class);
                break;
        }

        Bundle bundle = event.asBundle();
        intent.putExtras(bundle);

        this.startActivity(intent);
    }

    @Override
    public void onQueryFailure(Throwable failure) {
        // TODO: Display a toast.
    }
}
