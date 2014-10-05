package com.robwilliamson.healthyesther;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends Activity {
    private AsyncTask<Void, Void, Void> mOpeningQuery;

    abstract protected Query getOpeningQuery();
    abstract protected void onOpeningQueryComplete(Cursor cursor);

    @Override
    public void onResume() {
        super.onResume();

        mOpeningQuery = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Query query = getOpeningQuery();
                final Cursor cursor = query.query(HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onOpeningQueryComplete(cursor);
                    }
                });
                return null;
            }
        };

        mOpeningQuery.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpeningQuery != null && mOpeningQuery.getStatus() != AsyncTask.Status.FINISHED) {
            mOpeningQuery.cancel(false); // Let it run to completion if the DB operation has started.
        }
    }
}
