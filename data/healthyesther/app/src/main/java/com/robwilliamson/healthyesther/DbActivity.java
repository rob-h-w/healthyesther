package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.QueryUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends BusyActivity {
    private static final String LOG_TAG = DbActivity.class.getName();

    private volatile AsyncTask<Void, Void, Void> mTask = null;
    private Iterator<Query> mQueryIterator = null;

    public static class Error extends RuntimeException {}
    public static class QueriesRequestedWhileBusy extends Error {}
    public static class QueriesRequestedWhenQueriesPending extends Error {}

    /**
     * An array of query users that need to run queries every time this activity is resumed.
     * @return The query users that use queries on resume, or an empty array if no query is required.
     */
    abstract protected QueryUser[] getOnResumeQueryUsers();

    @Override
    protected void onDestroy() {
        cancel(mTask);
        mTask = null;
        mQueryIterator = null;

        super.onDestroy();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_backup_to_dropbox) {
            runAsTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        HealthDbHelper.getInstance(DbActivity.this).backupToDropbox();
                    } catch (IOException e) {
                        // TODO: Do some error handling UI.
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
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

        doQueries(queries);
    }

    protected final void doQueries(final ArrayList<Query> queries) {
        if (isBusy()) {
            throw new QueriesRequestedWhileBusy();
        }

        if (mQueryIterator != null) {
            throw new QueriesRequestedWhenQueriesPending();
        }

        mQueryIterator = queries.iterator();

        nextQuery();
    }

    protected final void query(final Query query) {
        runAsTask(new Runnable() {
            @Override
            public void run() {
                doQuery(query);
                nextQuery();
            }
        });
    }

    protected final void runAsTask(final Runnable runnable) {
        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                setBusy(true);
                runnable.run();
                setBusy(false);

                if (mTask == this) {
                    mTask = null;
                }

                return null;
            }
        };

        mTask.execute();
    }

    private void nextQuery() {
        if (mQueryIterator == null || !mQueryIterator.hasNext()) {
            mQueryIterator = null;
            return;
        }

        Query next = mQueryIterator.next();
        query(next);
    }

    private void doQuery(final Query query) {
        if (query == null) {
            return;
        }

        final Throwable[] error = new Throwable[] { null };
        final Cursor[] cursor = new Cursor[] { null };

        try {
            cursor[0] = query.query(HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase());
        } catch (Throwable e) {
            error[0] = e;
            Log.e(LOG_TAG, "Query threw", e);
        }

        if (mTask.isCancelled()) {
            return;
        }

        if (error[0] == null) {
            query.postQueryProcessing(cursor[0]);
        }

        if (mTask.isCancelled()) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
