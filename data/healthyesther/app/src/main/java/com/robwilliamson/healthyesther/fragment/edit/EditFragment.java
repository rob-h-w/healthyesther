package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;

import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.healthyesther.fragment.AbstractQueryFragment;

public abstract class EditFragment<T> extends AbstractQueryFragment {
    private T mWatcher = null;

    protected interface WatcherCaller<T> {
        public void call(T watcher);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mWatcher = (T)activity;
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
}
