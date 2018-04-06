/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.Executor;
import com.robwilliamson.healthyesther.dropbox.DropboxSyncActivity;
import com.robwilliamson.healthyesther.fragment.DbFragment;
import com.robwilliamson.healthyesther.fragment.dialog.ConfirmationDialogFragment;

import javax.annotation.Nonnull;

/**
 * Activities that use databases.
 */
public abstract class DbActivity extends BusyActivity
        implements ConfirmationDialogFragment.Observer,
        TransactionExecutor.Observer, DbFragment.ExecutorProvider {
    private static final String LOG_TAG = DbActivity.class.getName();
    private static final String CONFIRMATION_DIALOG = "CONFIRMATION_DIALOG";

    static {
        // Ensure the time converter is loaded.
        new DateTimeConverter();
    }

    private Executor mExecutor;
    private volatile AsyncTask<Void, Void, Void> mTask = null;

    private static void setEnabled(
            Menu menu,
            int itemId,
            @SuppressWarnings("SameParameterValue") boolean enabled) {
        MenuItem item = menu.findItem(itemId);

        if (item != null) {
            item.setEnabled(enabled);
        }
    }

    @Override
    public void onTransactionFail(final Throwable e) {
        Log.e(LOG_TAG, "transaction failed", e);
        setBusy(false);

        runOnUiThread(() -> {
            throw new DatabaseException(e);
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
    protected void onCreate(Bundle savedInstanceState) {
        mExecutor = new Executor(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean returnValue = super.onCreateOptionsMenu(menu);

        setEnabled(
                menu,
                R.id.action_backup_to_dropbox,
                true);
        setEnabled(
                menu,
                R.id.action_restore_from_dropbox,
                true);

        return returnValue;
    }

    @Override
    protected void onDestroy() {
        if (mExecutor != null) {
            mExecutor.cancel();
        }
        mExecutor = null;
        cancel(mTask);
        mTask = null;

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_backup_to_dropbox) {
            Intent startBackupIntent = new Intent(this, DropboxSyncActivity.class);
            startBackupIntent.putExtra(DropboxSyncActivity.RESTORE, false);
            startActivity(startBackupIntent);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogOk(ConfirmationDialogFragment dialogFragment) {
        runAsTask(() -> {
            Intent startRestoreIntent = new Intent(
                    DbActivity.this,
                    DropboxSyncActivity.class);
            startRestoreIntent.putExtra(DropboxSyncActivity.RESTORE, true);
            startActivity(startRestoreIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Nonnull
    public TransactionExecutor getExecutor() {
        return com.robwilliamson.healthyesther.Utils.checkNotNull(mExecutor);
    }

    protected final synchronized void runAsTask(final Runnable runnable) {
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

    private void cancel(AsyncTask<Void, Void, Void> task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
        }
    }

    public static class DatabaseException extends RuntimeException {
        DatabaseException(Throwable throwable) {
            super(throwable);
        }
    }
}
