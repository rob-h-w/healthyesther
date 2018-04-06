/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import java.util.HashSet;
import java.util.Set;

public class AsyncExecutor {
    private final Object mLock = new Object();
    private final Set<Runnable> mTasks = new HashSet<>();

    /**
     * Executes all extant tasks.
     */
    public void execute() {
        Set<Runnable> tasks;

        while (!mTasks.isEmpty()) {
            synchronized (mLock) {
                tasks = new HashSet<>(mTasks);
                mTasks.clear();
            }

            for (Runnable task : tasks) {
                task.run();
            }
        }
    }

    /**
     * Schedule a task for subsequent execution.
     *
     * @param task Task that will be run later.
     */
    public void schedule(Runnable task) {
        synchronized (mLock) {
            mTasks.add(task);
        }
    }
}
