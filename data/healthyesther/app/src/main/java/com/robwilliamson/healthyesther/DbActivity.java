package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends BusyActivity {
    private static final String LOG_TAG = DbActivity.class.getName();

    private volatile AsyncTask<Void, Void, Void> mOpeningQuery;
    private volatile AsyncTask<Void, Void, Void> mQuery;

    /**
     * A query that is run every time this activity is resumed.
     * @return The query to run, or null if no query is required.
     */
    abstract protected Query getOnResumeQuery();

    @Override
    protected void onPause() {
        cancel(mOpeningQuery);
        cancel(mQuery);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mOpeningQuery = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                doQuery(getOnResumeQuery(), this);
                mOpeningQuery = null;
                return null;
            }
        };

        mOpeningQuery.execute();
    }

    protected void query(final Query query) {
        mQuery = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                doQuery(query, this);
                mQuery = null;
                return null;
            }
        };

        mQuery.execute();
    }

    private void doQuery(final Query query, final AsyncTask task) {
        if (query == null) {
            return;
        }

        final Throwable[] error = new Throwable[] { null };
        final Cursor[] cursor = new Cursor[] { null };

        setBusy(true);

        try {
            cursor[0] = query.query(HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase());
        } catch (Throwable e) {
            error[0] = e;
            Log.e(LOG_TAG, "Query threw", e);
        }

        if (task.isCancelled()) {
            return;
        }

        if (error[0] == null) {
            query.postQueryProcessing(cursor[0]);
        }

        if (task.isCancelled()) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBusy(false);

                if (!isActive()) {
                    return;
                }

                if (error[0] == null) {
                    query.onQueryComplete(cursor[0]);
                } else {
                    query.onQueryFailed(error[0]);
                }
            }
        });
    }

    private void cancel(AsyncTask<Void, Void, Void> task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
        }
    }
}
