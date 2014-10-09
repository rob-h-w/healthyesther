package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.robwilliamson.db.definition.Modification;

public abstract class EditFragment<T> extends Fragment {
    private T mWatcher = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mWatcher = (T)activity;
    }

    public abstract Modification getModification();
    public abstract boolean validate();
    protected abstract void updateWatcher(T watcher);

    protected void updateWatcher() {
        if (mWatcher != null) {
            updateWatcher(mWatcher);
        }
    }
}
