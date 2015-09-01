package com.robwilliamson.healthyesther.db.includes;

/**
 * A class that can perform modifications as part of a transaction.
 */
public abstract class BaseTransactable {
    private boolean mIsInDatabase = false;
    private boolean mIsModified = false;
    private boolean mDeleted = false;

    public final void applyTo(Transaction transaction) {
        boolean inDb = isInDatabase();
        boolean isModified = isModified();
        boolean isDeleted = isDeleted();

        boolean insert = !inDb && !isDeleted;
        boolean modify = inDb && isModified && !isDeleted;
        boolean remove = isDeleted;

        if (insert) {
            insert(transaction);
        } else if (modify) {
            //modify(transaction);
        } else if (remove) {
            remove(transaction);
        }
    }

    public void delete() {
        mDeleted = true;
    }

    protected abstract Object insert(Transaction transaction);

    /*protected abstract void modify(Transaction transaction);*/
    protected abstract void remove(Transaction transaction);

    protected void setIsInDatabase(boolean inDatabase) {
        mIsInDatabase = inDatabase;
    }

    protected boolean isInDatabase() {
        return mIsInDatabase;
    }

    protected void setIsModified(boolean modified) {
        mIsModified = modified;
    }

    protected boolean isModified() {
        return mIsModified;
    }

    protected void setIsDeleted(boolean deleted) {
        mDeleted = deleted;
    }

    protected boolean isDeleted() {
        return mDeleted;
    }

    public final class RemoveFailed extends RuntimeException {
        public RemoveFailed(int expected, int actual) {
            super("Attempted to remove " + expected + " rows, actually " + actual + " rows were selected.");
        }

        public RemoveFailed(Throwable cause) {
            super(cause);
        }
    }
}
