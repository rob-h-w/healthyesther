package com.robwilliamson.healthyesther.db.integration;

import android.os.AsyncTask;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Executor extends TransactionExecutor {

    @Nullable
    private volatile AsyncTask<Void, Void, Void> mTask;

    public Executor(@Nonnull Observer observer) {
        super(observer);
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
                    synchronized (Executor.this) {
                        mTask = null;
                    }
                }
                return null;
            }
        };

        mTask.execute();
    }

    @Override
    @Nonnull
    protected Database getDatabase() {
        return HealthDbHelper.getDatabase();
    }

    @Override
    synchronized public void cancel() {
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }
}
