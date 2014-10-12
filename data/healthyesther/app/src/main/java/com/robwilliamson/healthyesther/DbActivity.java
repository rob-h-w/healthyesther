package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.QueryUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends BusyActivity {
    private static final String LOG_TAG = DbActivity.class.getName();

    private volatile AsyncTask<Void, Void, Void> mQuery = null;
    private Iterator<Query> mQueryIterator = null;

    /**
     * An array of query users that need to run queries every time this activity is resumed.
     * @return The query users that use queries on resume, or an empty array if no query is required.
     */
    abstract protected QueryUser[] getOnResumeQueryUsers();

    @Override
    protected void onDestroy() {
        cancel(mQuery);
        mQuery = null;
        mQueryIterator = null;

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isBusy()) {
            // We resumed when an operation was still pending. Let it continue.
            return;
        }

        if (mQueryIterator != null) {
            // We resumed after an async task completed, but there are more to do. Continue.
            nextQuery();
            return;
        }

        ArrayList<Query> queries = new ArrayList<Query>();

        QueryUser[] queryUsers = getOnResumeQueryUsers();

        for (QueryUser user : queryUsers) {
            Query[] userQueries = user.getQueries();
            if (userQueries != null) {
                queries.addAll(Arrays.asList(userQueries));
            }
        }

        mQueryIterator = queries.iterator();

        nextQuery();
    }

    protected final void query(final Query query) {
        mQuery = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                doQuery(query, this);
                mQuery = null;
                nextQuery();
                return null;
            }
        };

        mQuery.execute();
    }

    private void nextQuery() {
        if (mQueryIterator == null || !mQueryIterator.hasNext()) {
            mQueryIterator = null;
            return;
        }

        Query next = mQueryIterator.next();
        query(next);
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
