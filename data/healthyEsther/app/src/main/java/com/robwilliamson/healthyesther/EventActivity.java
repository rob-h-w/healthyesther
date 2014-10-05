package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.SelectEventAndType;
import com.robwilliamson.db.use.SelectQuery;

import java.util.Calendar;


public class EventActivity extends DbActivity {
    private ListView mEventListView;
    private SelectQuery mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mQuery = new SelectEventAndType();

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

            Contract contract = Contract.getInstance();

            long pieId = contract.MEAL.insert(db, "PAH!");
            long stewId = contract.MEAL.insert(db, "Stoo");

            long paracetamolId = contract.MEDICATION.insert(db, "Paracetamol");

            for (int i = 0; i < 1000; i++) {
                int second = (31*i/10) % 60;
                int minute = i % 60;
                int hour = (127*i/10) % 24;
                int day = (i/10) % 28 + 1;
                int type = ((13*second*hour*i/10) % 2) + 1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(2014, Calendar.MARCH, day, hour, minute, second);
                long eventId = contract.EVENT.insert(db, calendar, type, "Event " + i);

                if (type == 1) {
                    contract.MEAL_EVENT.insert(db, (minute % 2 > 0) ? pieId : stewId, eventId);
                } else {
                    contract.MEDICATION_EVENT.insert(db, paracetamolId, eventId);
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void cleanOldData(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            Contract contract = Contract.getInstance();
            contract.delete(db);
            contract.create(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected Query getOpeningQuery() {
        return mQuery;
    }

    @Override
    protected void onOpeningQueryComplete(Cursor cursor) {
        if (mEventListView != null) {
            String[] allColumns = mQuery.getResultColumns();
            mEventListView.setAdapter(new SimpleCursorAdapter(this, R.layout.list_item, cursor,
                    new String[] { allColumns[2],                allColumns[3] },
                    new int[]    { R.id.event_list_element_name, R.id.event_list_element_type}));
        }
    }
}
