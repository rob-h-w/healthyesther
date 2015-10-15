package com.robwilliamson.healthyesther.db.integration;

import android.os.AsyncTask;

import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import javax.annotation.Nonnull;

public class Executor extends TransactionExecutor {
    private volatile AsyncTask<Void, Void, Void> mTask;

    public Executor(@Nonnull Database database, @Nonnull Observer observer) {
        super(database, observer);
    }

    @Override
    synchronized protected void runAsynchronously(@Nonnull final Runnable runnable) {
        if (mTask != null) {
            throw new IllegalStateException("There is an extant task.");
        }

        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    runnable.run();
                } finally {
                    mTask = null;
                }
                return null;
            }
        };

        mTask.execute();
    }

    @Override
    synchronized public void cancel() {
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }
}
