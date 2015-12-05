package com.robwilliamson.healthyesther.db.includes;

import java.io.Serializable;

public abstract class BaseRow<T extends Key> extends BaseTransactable implements PrimaryKeyOwner, Serializable {
    private T mKey;
    private T mNextKey;

    @Override
    public Key getPrimaryKey() {
        return mKey;
    }

    protected void setPrimaryKey(T key) {
        if (mKey == key || (mKey != null && mKey.equals(key))) {
            return;
        }

        mKey = key;
        setIsModified(true);
    }

    public T getConcretePrimaryKey() {
        return mKey;
    }

    public T getNextPrimaryKey() {
        return mNextKey == null ? mKey : mNextKey;
    }

    protected void setNextPrimaryKey(T key) {
        if (mNextKey == key || (mNextKey != null && mNextKey.equals(key))) {
            return;
        }
        mNextKey = key;
    }

    public abstract boolean isValid();

    protected void updatePrimaryKeyFromNext() {
        mKey = mNextKey;
        mNextKey = null;
    }
}
