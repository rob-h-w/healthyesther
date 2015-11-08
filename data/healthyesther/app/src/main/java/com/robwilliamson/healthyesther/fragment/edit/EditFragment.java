package com.robwilliamson.healthyesther.fragment.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.fragment.AbstractQueryFragment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class EditFragment<R extends BaseRow, T> extends AbstractQueryFragment {
    @NonNull
    Class<T> mType;

    @Nullable
    private T mWatcher = null;

    @Nullable
    private R mRow;

    @Deprecated
    private boolean mModified;

    public EditFragment(@NonNull Class<T> type) {
        mType = type;
    }

    @Nullable
    public R getRow() {
        return mRow;
    }

    public void setRow(@Nonnull R row) {
        mRow = row;
    }

    @Deprecated
    public boolean getModified() {
        return mModified;
    }

    @Deprecated
    public void setModified(boolean modified) {
        mModified = modified;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mWatcher = Utils.checkAssignable(context, mType);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();

        mWatcher = null;
    }

    @Nullable
    public abstract Modification getModification();

    public abstract boolean validate();

    protected abstract void updateWatcher(@NonNull T watcher);

    protected void callWatcher(@NonNull WatcherCaller<T> call) {
        if (mWatcher != null) {
            call.call(mWatcher);
        }
    }

    protected void updateWatcher() {
        if (mWatcher != null) {
            updateWatcher(mWatcher);
        }
    }

    @Nullable
    protected <U extends View> U getTypeSafeView(int id, @NonNull Class<U> type) {
        if (getView() == null) {
            return null;
        }
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeView(getView(), id, type);
    }

    protected interface WatcherCaller<T> {
        void call(@NonNull T watcher);
    }
}
