package com.robwilliamson.healthyesther;

import java.util.HashSet;
import java.util.Set;

public class AsyncExecutor {
    private final Object mLock = new Object();
    private final Set<Runnable> mTasks = new HashSet<>();
    private volatile boolean mExecuting;

    /**
     * Synchronize on this before attempting to schedule tasks.
     * @return Object to synchronize on.
     */
    public Object lock() {
        return mLock;
    }

    /**
     * Executes all extant tasks.
     */
    public synchronized void execute() {
        synchronized (mLock) {
            mExecuting = true;

            for (Runnable task : mTasks) {
                task.run();
            }

            mExecuting = false;
        }
    }

    /**
     * Schedule a task for subsequent execution.
     * @param task Task that will be run later.
     */
    public synchronized void schedule(Runnable task) {
        if (mExecuting) {
            throw new IllegalStateException("Attempt to add a task while executing tasks");
        }

        mTasks.add(task);
    }
}
