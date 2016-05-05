/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import java.io.Serializable;

import javax.annotation.Nonnull;

/**
 * A class that can perform modifications as part of a transaction.
 */
public abstract class BaseTransactable implements Serializable {
    private boolean mIsInDatabase = false;
    private boolean mIsModified = false;
    private boolean mDeleted = false;

    public final void applyTo(@Nonnull Transaction transaction) {
        boolean inDb = isInDatabase();
        boolean isModified = isModified();
        boolean isDeleted = isDeleted();

        boolean insert = !inDb && !isDeleted;
        boolean modify = inDb && isModified && !isDeleted;

        if (insert) {
            insert(transaction);
        } else if (modify) {
            update(transaction);
        } else if (isDeleted) {
            remove(transaction);
        }
    }

    public void delete() {
        mDeleted = true;
    }

    @Nonnull
    protected abstract Object insert(@Nonnull Transaction transaction);

    protected abstract void update(@Nonnull Transaction transaction);

    protected abstract void remove(@Nonnull Transaction transaction);

    protected void setIsInDatabase(boolean inDatabase) {
        mIsInDatabase = inDatabase;
    }

    public boolean isInDatabase() {
        return mIsInDatabase;
    }

    protected void setIsModified(boolean modified) {
        mIsModified = modified;
    }

    public boolean isModified() {
        return mIsModified;
    }

    protected void setIsDeleted(boolean deleted) {
        mDeleted = deleted;
    }

    public boolean isDeleted() {
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

    public final class UpdateFailed extends RuntimeException {
        public UpdateFailed(String message) {
            super(message);
        }

        public UpdateFailed(int expected, int actual) {
            super("Attempted to update " + expected + " rows, actually " + actual + " rows were selected.");
        }
    }
}
