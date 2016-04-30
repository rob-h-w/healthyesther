package com.robwilliamson.healthyesther.fragment.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.fragment.AbstractQueryFragment;

import javax.annotation.Nullable;

public abstract class EditFragment<T> extends AbstractQueryFragment {
    Class<T> mType;
    private T mWatcher = null;
    private boolean mModified;

    public EditFragment(Class<T> type) {
        mType = type;
    }

    public boolean getModified() {
        return mModified;
    }

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

    public abstract Modification getModification();

    public abstract boolean validate();

    protected abstract void updateWatcher(T watcher);

    protected void callWatcher(WatcherCaller<T> call) {
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
    protected <T extends View> T getTypeSafeView(int id, @NonNull Class<T> type) {
        if (getView() == null) {
            return null;
        }
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeView(getView(), id, type);
    }

    protected interface WatcherCaller<T> {
        void call(T watcher);
    }
}
