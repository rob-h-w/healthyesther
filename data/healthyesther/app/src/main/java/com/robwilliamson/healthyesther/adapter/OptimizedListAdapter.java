/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.robwilliamson.healthyesther.Utils;

public abstract class OptimizedListAdapter<T, V extends View, D> extends ArrayAdapter<D> {
    private final Class<T> mTagType;
    private final Class<V> mViewType;
    private final Activity mActivity;
    private final int mLayout;

    OptimizedListAdapter(Activity context, int layout, Class<T> tagType, Class<V> viewType) {
        super(context, layout);
        mTagType = Utils.checkNotNull(tagType);
        mViewType = Utils.checkNotNull(viewType);
        mLayout = layout;
        mActivity = Utils.checkNotNull(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        V view = Utils.checkedCast(convertView, mViewType);

        if (view == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            view = Utils.checkNotNull(
                    Utils.checkedCast(inflater.inflate(mLayout, null), mViewType));
            T tag = Utils.checkNotNull(getTagFor(view));
            view.setTag(tag);
        }

        T tag = Utils.checkedCast(view.getTag(), mTagType);
        populateTag(tag, getItem(position));

        return view;
    }

    /**
     * Get a tag for a view. A tag should contain references to every view modified when new data is
     * used.
     *
     * @param view The view requiring a tag.
     * @return A new tag for the view.
     */
    protected abstract T getTagFor(V view);

    /**
     * Use a tag to populate the features of a view. The tag will have been returned by
     * {@link #getTagFor(V view)}.
     *
     * @param tag  The tag to use with the view.
     * @param data The data to populate the tag's views with.
     */
    protected abstract void populateTag(T tag, D data);
}
