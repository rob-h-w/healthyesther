/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment;

import android.content.Context;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DbFragment extends BaseFragment {
    @Nullable
    private ExecutorProvider mProvider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProvider = Utils.checkAssignable(context, ExecutorProvider.class);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();

        mProvider = null;
    }

    /**
     * Get the database transaction executor from the surrounding activity.
     *
     * @return The executor or null if it's not available either because this fragment is not attached or because the provider returned null.
     */
    @Nullable
    public TransactionExecutor getExecutor() {
        if (mProvider == null) {
            return null;
        }

        return mProvider.getExecutor();
    }

    public interface ExecutorProvider {
        @Nonnull
        TransactionExecutor getExecutor();
    }
}
