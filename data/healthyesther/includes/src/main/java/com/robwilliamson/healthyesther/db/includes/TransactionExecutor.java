package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public abstract class TransactionExecutor {
    @Nonnull
    private final Database mDb;
    @Nonnull
    private final Observer mObserver;

    public TransactionExecutor(@Nonnull Database database, @Nonnull Observer observer) {
        mDb = database;
        mObserver = observer;
    }

    protected abstract void runAsynchronously(@Nonnull Runnable runnable);

    public abstract void cancel();

    public void perform(final @Nonnull Operation operation) {
        mObserver.onTransactionStart();
        runAsynchronously(new Runnable() {
            @Override
            public void run() {
                Transaction transaction = mDb.getTransaction();
                try {
                    operation.doTransactionally(mDb, transaction);
                    transaction.commit();
                    mObserver.onTransactionComplete();
                } catch (Throwable e) {
                    transaction.rollBack();
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

    public static abstract class QueryOperation<R extends BaseRow> implements Operation {
        private R[] mResults;

        public
        @Nonnull
        R[] getResults() {
            if (mResults == null) {
                throw new NullQueryResultException();
            }
            return mResults;
        }

        /**
         * Implementors should call this when their operation succeeds.
         *
         * @param results The results of the query.
         */
        protected void setResults(@Nonnull R[] results) {
            mResults = results;
        }

        public static class NullQueryResultException extends RuntimeException {
            public NullQueryResultException() {
                super("The result array was null. Did you forget to call setResults()?");
            }
        }
    }
}
