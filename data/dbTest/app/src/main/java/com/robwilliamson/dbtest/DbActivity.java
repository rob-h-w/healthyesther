package com.robwilliamson.dbtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.robwilliamson.db.HealthDbHelper;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends Activity {

    //abstract protected void openQuery(SQLiteDatabase db);
    //abstract protected void closeTransaction(SQLiteDatabase db);

    protected void queryAndClose(Intent success, Intent failure) {
        // Do the query...
        boolean successful = true;
        if (successful) {
            startActivity(success);
        } else {
            startActivity(failure);
        }
    }
}
