/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.os.AsyncTask;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Executor extends TransactionExecutor {
    @Nonnull
    private final Deque<Runnable> mTasks = new ArrayDeque<>();

    @Nullable
    private volatile AsyncTask<Void, Void, Void> mRunner;

    public Executor(@Nonnull Observer observer) {
        super(observer);
    }

    @Override
    protected void runAsynchronously(@Nonnull Runnable runnable) {
        synchronized (mTasks) {
            boolean start = mTasks.isEmpty();
            mTasks.add(runnable);

            if (start) {
                //noinspection ConstantConditions
                if (mRunner == null || mRunner.getStatus() == AsyncTask.Status.FINISHED) {

                    mRunner = createRunner();
                }
            }
        }
    }

    protected AsyncTask<Void, Void, Void> createRunner() {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Runnable runnable = null;
                synchronized (mTasks) {
                    if (!mTasks.isEmpty()) {
                        runnable = mTasks.pop();
                    }
                }

                if (runnable != null) {
                    runnable.run();
                }
                return null;
            }

            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             * <p/>
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param aVoid The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                synchronized (mTasks) {
                    if (!mTasks.isEmpty()) {
                        mRunner = createRunner();
                    }
                }
            }
        };
        asyncTask.execute();
        return asyncTask;
    }

    @Override
    @Nonnull
    protected Database getDatabase() {
        return HealthDbHelper.getDatabase();
    }

    @Override
    public void cancel() {
        synchronized (mTasks) {
            if (mRunner != null) {
                //noinspection ConstantConditions
                mRunner.cancel(true);
            }

            mTasks.clear();
        }
    }
}
