package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.fragment.DbFragment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class EditFragment<R extends BaseRow> extends DbFragment {
    private static final String ROW = "row";
    @Nullable
    private R mRow;

    @Nullable
    public R getRow() {
        return mRow;
    }

    public void setRow(@Nonnull R row) {
        mRow = row;
    }

    //noinspection
    public boolean hasRow() {
        return mRow != null;
    }

    public boolean isValid() {
        return getRow() != null && getRow().isValid();

    }

    protected abstract R createRow();

    protected boolean canCreateRow() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (savedInstanceState != null) {
            //noinspection unchecked
            mRow = (R) savedInstanceState.getSerializable(ROW);
        }

        if (mRow == null && canCreateRow()) {
            mRow = createRow();
        }

        return view;
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link android.app.Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ROW, mRow);
    }

    @Nullable
    protected <U extends View> U getTypeSafeView(int id, @NonNull Class<U> type) {
        if (getView() == null) {
            return null;
        }
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeView(getView(), id, type);
    }
}
