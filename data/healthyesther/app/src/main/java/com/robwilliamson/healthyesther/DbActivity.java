package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.db.use.QueryUserProvider;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends BusyActivity implements QueryUserProvider {
    private static final String LOG_TAG = DbActivity.class.getName();

    private volatile AsyncTask<Void, Void, Void> mTask = null;
    private Deque<Query> mQueries = null;

    @Override
    protected void onDestroy() {
        cancel(mTask);
        mTask = null;
        mQueries = null;

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

        QueryUser[] queryUsers = getQueryUsers();

        ArrayList<Query> queries = new ArrayList<>();

        for (QueryUser user : queryUsers) {
            Query[] userQueries = user.getQueries();
            if (userQueries != null) {
                queries.addAll(Arrays.asList(userQueries));
            }
        }

        doQueries(queries);
    }

    protected final void doQueries(final List<Query> queries) {
        if (mQueries == null) {
            mQueries = new ArrayDeque<>(queries);
        } else {
            mQueries.addAll(queries);
        }

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
        if (mQueries == null || mQueries.isEmpty()) {
            mQueries = null;
            return;
        }

        Query next = mQueries.pop();
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
