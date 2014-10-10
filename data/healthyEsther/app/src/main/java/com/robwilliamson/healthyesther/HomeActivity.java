package com.robwilliamson.healthyesther;

import android.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.robwilliamson.db.*;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.Table;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.SelectEventAndType;
import com.robwilliamson.healthyesther.fragment.AddEventFragment;
import com.robwilliamson.healthyesther.fragment.NavigationDrawerFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeActivity extends DbActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
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

            AddEventFragment addEventFragment = new AddEventFragment();
            addEventFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.home_activity_content_layout, addEventFragment).commit();
        }
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
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
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
    protected Query getOnResumeQuery() {
        return new SelectEventAndType(DateTime.now().minusWeeks(1).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0),
                DateTime.now().minusDays(1).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0)) {
            private static final int DAYS = 7;
            private final DateTimeFormatter FORMAT = ISODateTimeFormat.dateTime();
            private HashMap<String, Integer> mEntriesPerDay = new HashMap<String, Integer>(DAYS); // 7 days.
            private DateTime mNow;

            @Override
            public void postQueryProcessing(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    for (int i = 1; i <= DAYS; i++) {
                        mEntriesPerDay.put(FORMAT.print(now().minusDays(i)), 0);
                    }

                    int whenIndex = cursor.getColumnIndex(Table.cleanName(Event.WHEN));
                    do {
                        DateTime when = Utils.Time.dateTimeFromDatabaseString(cursor.getString(whenIndex)).withTime(0, 0, 0, 0);
                        Integer count = mEntriesPerDay.get(FORMAT.print(when));
                        mEntriesPerDay.put(FORMAT.print(when), count+1);
                    } while (cursor.moveToNext());
                }
            }

            @Override
            public void onQueryComplete(Cursor cursor) {// init example series data
                GraphView.GraphViewData[] data = new GraphView.GraphViewData[DAYS];
                String [] dateStrings = new String[DAYS];
                DateTimeFormatter formatter = DateTimeFormat.shortDate();

                for (int i = 1; i <= DAYS; i++) {
                    DateTime day = now().minusDays(i);
                    String dayStr = FORMAT.print(day);
                    data[i - 1] = new GraphView.GraphViewData(i, mEntriesPerDay.get(dayStr));
                    dateStrings[i - 1] = formatter.print(day);
                }

                GraphViewSeries activitySeries = new GraphViewSeries(data);

                GraphView graphView = new LineGraphView(
                        HomeActivity.this,
                        getString(R.string.activity_last_week)
                );
                graphView.addSeries(activitySeries);
                graphView.setHorizontalLabels(dateStrings);

                getActivityContentLayout().addView(graphView);
            }

            @Override
            public void onQueryFailed(Throwable error) {

            }

            private DateTime now() {
                if (mNow == null) {
                    mNow = DateTime.now().withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
                }

                return mNow;
            }
        };
    }
}
