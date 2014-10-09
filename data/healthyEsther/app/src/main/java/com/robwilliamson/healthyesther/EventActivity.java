package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.SelectEventAndType;
import com.robwilliamson.db.use.SelectQuery;

import org.joda.time.DateTime;


public class EventActivity extends DbActivity {
    private ListView mEventListView;
    private SelectQuery mQuery;

    public EventActivity() { super(false); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mQuery = new SelectEventAndType() {
            @Override
            public void postQueryProcessing(Cursor cursor) {

            }

            @Override
            public void onQueryComplete(Cursor cursor) {
                if (mEventListView != null) {
                    String[] allColumns = this.getResultColumns();
                    mEventListView.setAdapter(new SimpleCursorAdapter(
                            EventActivity.this,
                            R.layout.list_item, cursor,
                            new String[] { allColumns[2],                allColumns[3] },
                            new int[]    { R.id.event_list_element_name, R.id.event_list_element_type}));
                }
            }

            @Override
            public void onQueryFailed(Throwable error) {
                Toast.makeText(EventActivity.this, "Waah!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventListView = (ListView) findViewById(R.id.eventListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
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
        return mQuery;
    }
}
