package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public abstract class TransactionExecutor {
    @Nonnull
    private final Database mDb;
    @Nonnull
    private final Observer mObserver;

    TransactionExecutor(@Nonnull Database database, @Nonnull Observer observer) {
        mDb = database;
        mObserver = observer;
    }

    protected abstract void runAsynchronously(@Nonnull Runnable runnable);

    public abstract void cancel();

    void perform(final @Nonnull Operation operation) {
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                try {
                    Transaction transaction = mDb.getTransaction();
                    mObserver.onTransactionStart();
                    operation.doTransactionally(transaction);
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
        void doTransactionally(@Nonnull Transaction transaction);
    }
}
