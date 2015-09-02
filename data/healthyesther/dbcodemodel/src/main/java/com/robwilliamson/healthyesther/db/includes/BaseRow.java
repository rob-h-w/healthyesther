package com.robwilliamson.healthyesther.db.includes;

public abstract class BaseRow <T extends Key> extends BaseTransactable implements PrimaryKeyOwner {
    private T mKey;
    private T mNextKey;

    protected void setPrimaryKey(T key) {
        if (mKey == key || (mKey != null && mKey.equals(key))) {
            return;
        }

        mKey = key;
        setIsModified(true);
    }

    @Override
    public Key getPrimaryKey() {
        return mKey;
    }

    protected T getConcretePrimaryKey() {
        return mKey;
    }

    protected void setNextPrimaryKey(T key) {
        if (mNextKey == key || (mNextKey != null && mNextKey.equals(key))) {
            return;
        }
        mNextKey = key;
    }

    protected T getNextPrimaryKey() {
        return mNextKey;
    }
}
