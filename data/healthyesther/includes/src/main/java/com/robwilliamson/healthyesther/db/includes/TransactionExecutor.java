/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public abstract class TransactionExecutor {
    @Nonnull
    private final Observer mObserver;

    public TransactionExecutor(@Nonnull Observer observer) {
        mObserver = observer;
    }

    protected abstract void runAsynchronously(@Nonnull Runnable runnable);

    @Nonnull
    protected abstract Database getDatabase();

    public abstract void cancel();

    public void perform(final @Nonnull Operation operation) {
        mObserver.onTransactionStart();
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                Database db = getDatabase();
                try (Transaction transaction = db.getTransaction()) {
                    operation.doTransactionally(db, transaction);
                    transaction.commit();
                    mObserver.onTransactionComplete();
                } catch (Throwable e) {
                    mObserver.onTransactionFail(e);
                }
            }
        });
    }

    public interface Observer {
        void onTransactionStart();

        void onTransactionComplete();

        void onTransactionFail(Throwable e);
    }

    public interface Operation {
        void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction);
    }
}
