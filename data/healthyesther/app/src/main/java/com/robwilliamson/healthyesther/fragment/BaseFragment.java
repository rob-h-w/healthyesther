/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nullable;

public abstract class BaseFragment extends Fragment {
    @Nullable
    private Watcher mWatcher;

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context The context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mWatcher = (Watcher) context;
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

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            return inflater.inflate(getFragmentLayout(), container, false);
        } catch (Throwable t) {
            Log.e(BaseFragment.class.getSimpleName(), t.toString());
            throw t;
        }
    }

    protected void updateAttachedActivity() {
        if (mWatcher != null) {
            mWatcher.onFragmentUpdated(this);
        }
    }

    protected abstract int getFragmentLayout();

    public interface Watcher {
        void onFragmentUpdated(BaseFragment fragment);
    }
}
