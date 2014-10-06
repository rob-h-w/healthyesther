package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends FragmentActivity {
    private final boolean mRunToCompletion;
    private BusyFragment mBusyFragment;
    private volatile AsyncTask<Void, Void, Void> mOpeningQuery;
    private volatile AsyncTask<Void, Void, Void> mQuery;

    /**
     * A query that is run every time this activity is resumed.
     * @return The query to run, or null if no query is required.
     */
    abstract protected Query getOnResumeQuery();

    public DbActivity(boolean runToCompletion) {
        mRunToCompletion = runToCompletion;
        mBusyFragment = new BusyFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancel(mOpeningQuery);
        cancel(mQuery);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mOpeningQuery = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                doQuery(getOnResumeQuery());
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
                doQuery(query);
                mQuery = null;
                return null;
            }
        };
    }

    private void doQuery(final Query query) {
        if (query == null) {
            return;
        }

        setBusy(true);
        final Cursor cursor = query.query(HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBusy(false);
                query.onQueryComplete(cursor);
            }
        });
    }

    private void cancel(AsyncTask<Void, Void, Void> task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mRunToCompletion);
        }
    }

    private synchronized void setBusy(boolean busy) {
        if (busy) {
            mBusyFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, mBusyFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(mBusyFragment).commit();
        }
    }
}
