package com.robwilliamson.healthyesther.fragment.edit;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;

public class EditScoreFragment extends EditFragment {

    private static final String VALUE = "value";

    private int mValue;
    private long mId;
    private String mName;
    private int mBestValue;
    private String mMinLabel;
    private String mMaxLabel;

    public static EditScoreFragment newInstance(long id,
                                                String name,
                                                int bestValue,
                                                String minLabel,
                                                String maxLabel) {
        EditScoreFragment fragment = new EditScoreFragment();
        Bundle args = new Bundle();
        args.putInt(VALUE, 0);
        args.putLong(HealthScore._ID, id);
        args.putString(HealthScore.NAME, name);
        args.putInt(HealthScore.BEST_VALUE, bestValue);
        args.putString(HealthScore.MIN_LABEL, minLabel);
        args.putString(HealthScore.MAX_LABEL, maxLabel);
        fragment.setArguments(args);
        return fragment;
    }

    public EditScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mValue = args.getInt(VALUE);
            mId = args.getLong(HealthScore._ID);
            mName = args.getString(HealthScore.NAME);
            mBestValue = args.getInt(HealthScore.BEST_VALUE);
            mMinLabel = args.getString(HealthScore.MIN_LABEL, null);
            mMaxLabel = args.getString(HealthScore.MAX_LABEL, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_score, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle args) {
        super.onSaveInstanceState(args);
        args.putInt(VALUE, mValue);
        args.putLong(HealthScore._ID, mId);
        args.putString(HealthScore.NAME, mName);
        args.putInt(HealthScore.BEST_VALUE, mBestValue);
        args.putString(HealthScore.MIN_LABEL, mMinLabel);
        args.putString(HealthScore.MAX_LABEL, mMaxLabel);
    }

    @Override
    public Modification getModification() {
        return null;
    }

    @Override
    public boolean validate() {
        return mValue != 0;
    }

    @Override
    protected void updateWatcher(Object watcher) {

    }

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }
}
