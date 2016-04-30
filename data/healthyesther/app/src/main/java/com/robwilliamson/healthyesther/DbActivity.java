package com.robwilliamson.healthyesther;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.integration.DatabaseWrapperClass;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.Executor;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.db.use.QueryUserProvider;
import com.robwilliamson.healthyesther.db.use.QueuedQueryExecutor;
import com.robwilliamson.healthyesther.fragment.dialog.ConfirmationDialogFragment;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends BusyActivity
        implements QueuedQueryExecutor, QueryUserProvider, ConfirmationDialogFragment.Observer,
        TransactionExecutor.Observer {
    private static final String LOG_TAG = DbActivity.class.getName();
    private static final String CONFIRMATION_DIALOG = "CONFIRMATION_DIALOG";

    static {
        // Ensure the time converter is loaded.
        new DateTimeConverter();
    }

    private Executor mExecutor;
    private volatile AsyncTask<Void, Void, Void> mTask = null;
    private Deque<Query> mQueries = null;

    private static void setEnabled(Menu menu, int itemId, boolean enabled) {
        MenuItem item = menu.findItem(itemId);

        if (item != null) {
            item.setEnabled(enabled);
        }
    }

    @Override
    public void onTransactionFail(final Throwable e) {
        Log.e(LOG_TAG, "transaction failed", e);
        setBusy(false);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                throw new DatabaseException(e);
            }
        });
    }

    @Override
    public void onTransactionStart() {
        setBusy(true);
    }

    @Override
    public void onTransactionComplete() {
        setBusy(false);
    }

    @Override
    public void enqueueQueries(List<Query> queries) {
        doQueries(queries);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean returnValue = super.onCreateOptionsMenu(menu);

        setEnabled(
                menu,
                R.id.action_backup_to_dropbox,
                Utils.File.Dropbox.isDropboxPresent());
        setEnabled(
                menu,
                R.id.action_restore_from_dropbox,
                Utils.File.Dropbox.isDbFileInDropboxAppFolder());

        return returnValue;
    }

    @Override
    protected void onDestroy() {
        if (mExecutor != null) {
            mExecutor.cancel();
        }
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

        if (item.getItemId() == R.id.action_restore_from_dropbox) {
            ConfirmationDialogFragment dialogFragment = ConfirmationDialogFragment.
                    create(
                            R.string.action_restore_from_dropbox,
                            R.string.confirm_restore_from_dropbox_message);
            dialogFragment.show(getSupportFragmentManager(), CONFIRMATION_DIALOG);
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onDialogOk(ConfirmationDialogFragment dialogFragment) {
        runAsTask(new Runnable() {
            @Override
            public void run() {
                try {
                    HealthDbHelper.getInstance(DbActivity.this).restoreFromDropbox();
                } catch (IOException e) {
                    // TODO: Do some error handling UI.
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mExecutor = new Executor(new DatabaseWrapperClass(HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase()), this);

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

    protected TransactionExecutor getExecutor() {
        return mExecutor;
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

        final Throwable[] error = new Throwable[]{null};
        final Cursor[] cursor = new Cursor[]{null};

        try {
            cursor[0] = query.query(HealthDbHelper.getInstance(getApplicationContext()).getWritableDatabase());
        } catch (Throwable e) {
            error[0] = e;
            Log.e(LOG_TAG, "Query threw", e);
        }

        if (mTask == null || mTask.isCancelled()) {
            return;
        }

        if (error[0] == null) {
            query.postQueryProcessing(cursor[0]);
        }

        if (mTask == null || mTask.isCancelled()) {
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

    public static class DatabaseException extends RuntimeException {
        public DatabaseException(Throwable throwable) {
            super(throwable);
        }
    }
}
