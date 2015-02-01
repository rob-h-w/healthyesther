package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.robwilliamson.healthyesther.Utils;

import java.util.List;

public abstract class OptimizedListAdapter<T, V extends View, D> extends ArrayAdapter<D> {

    private final Activity mActivity;
    private final int mLayout;

    public OptimizedListAdapter(Activity context, int layout) {
        super(context, layout);
        mLayout = layout;
        mActivity = context;
    }

    public OptimizedListAdapter(Activity context, int layout, List<D> list) {
        this(context, layout);
        this.addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V view = Utils.uncheckedCast(convertView);

        if (view == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            view = Utils.uncheckedCast(inflater.inflate(mLayout, parent));
            view.setTag(getTagFor(view));
        }

        T tag = Utils.uncheckedCast(view.getTag());
        populateTag(tag, getItem(position));

        return view;
    }

    /**
     * Get a tag for a view. A tag should contain references to every view modified when new data is
     * used.
     * @param view The view requiring a tag.
     * @return A new tag for the view.
     */
    protected abstract T getTagFor(V view);

    /**
     * Use a tag to populate the features of a view. The tag will have been returned by
     * {@link #getTagFor(V view)}.
     * @param tag The tag to use with the view.
     * @param data The data to populate the tag's views with.
     */
    protected abstract void populateTag(T tag, D data);
}
