package com.robwilliamson.dbtest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.EventType;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.SelectEventAndType;

import java.util.Calendar;


public class EventActivity extends DbActivity {
    private ListView mEventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        fakeData();
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private void fakeData() {
        SQLiteDatabase db = HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase();
        cleanOldData(db);
        try {
            db.beginTransaction();

            Contract contract = Contract.getInstance(db);

            for (int i = 0; i < 100; i++) {
                int second = 59 - i % 60;
                int minute = 10 * (i % 6);
                int hour = i % 24;
                int day = (int) (i * 0.28) + 1;
                int type = (i % 2) + 1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(2014, Calendar.MARCH, day, hour, minute, second);
                contract.EVENT.insert(calendar, type, "Event " + i);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void cleanOldData(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            Contract contract = Contract.getInstance(db);
            contract.delete();
            contract.create();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected Query getOpeningQuery() {
        return new SelectEventAndType();
    }

    @Override
    protected void onOpeningQueryComplete(Cursor cursor) {
        if (mEventListView != null) {
            ((BaseAdapter)mEventListView.getAdapter()).notifyDataSetChanged();
        }
    }
}
