package com.robwilliamson.dbtest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.HealthDbHelper;


public class EventActivity extends DbActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    @Override
    public void onResume() {
        super.onResume();

        fakeData();
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
        HealthDbHelper helper = HealthDbHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.beginTransaction();
//            db.execSQL("drop table if exists " + Contract.Event.Table.NAME + ";");
//            db.execSQL("drop table if exists " + Contract.EventTypeTable.Table.NAME + ";");

//            db.execSQL(Contract.Event.CREATE_TABLE);
//            db.execSQL(Contract.EventTypeTable.CREATE_TABLE);
            ContentValues values = new ContentValues();
            values.put(Contract.EventType.Column.NAME, "Eat a meal");
            db.insert(Contract.EventType.Table.NAME, null, values);
            /*db.execSQL("insert into " + Contract.EventType.Table.NAME
                    + "values ( NULL, \"Eat a meal\", \"meal_event\" )");*/

            values = new ContentValues();
            values.put(Contract.EventType.Column.NAME, "Take medication");
            db.insert(Contract.EventType.Table.NAME, null, values);

            /*db.execSQL("insert into " + Contract.EventType.Table.NAME
                    + "values ( NULL, \"Take medication\", \"medication_event\" )");*/
            for (int i = 0; i < 100; i++) {
                int minute = 10 * (i % 6);
                int hour = i % 24;
                int day = (int) (i * 0.28) + 1;
                int type = (i % 2) + 1;
                db.execSQL("insert into " + Contract.Event.Table.NAME
                + " ( "
                        + Contract.Event.Column.WHEN + ", "
                        + Contract.Event.Column.CREATED + ", "
                        + Contract.Event.Column.TYPE_ID + ", "
                        + Contract.Event.Column.NAME
                + " ) "
                + "values( "
                                + "\"2014-03-" + day + " " + hour + ":" + minute + ":00\", "
                                + "\"2014-04-" + day + " " + hour + ":" + minute + ":00\", "
                                + type + ", "
                                + "\"Event " + i + "\""
                + " )"
                );
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
