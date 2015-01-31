package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class OptimizedListAdapter<T, V extends View, D> extends ArrayAdapter<D> {

    public static interface Tagger<T, V extends View, D> {
        T getTagFor(V view);
        void populateViewUsingTag(T tag, V view, D data);
    }

    private final Activity mActivity;
    private final int mLayout;
    private final Tagger<T, V, D> mTagger;

    public OptimizedListAdapter(Activity context, int layout, Tagger<T, V, D> tagger) {
        super(context, layout);
        mLayout = layout;
        mActivity = context;
        mTagger = tagger;
    }

    public OptimizedListAdapter(Activity context, int layout, Tagger<T, V, D> tagger, List<D> list) {
        this(context, layout, tagger);
        this.addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V view = (V) convertView;

        if (view == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            view = (V) inflater.inflate(mLayout, parent);
            view.setTag(mTagger.getTagFor(view));
        }

        mTagger.populateViewUsingTag((T) view.getTag(), view, getItem(position));

        return view;
    }
}
