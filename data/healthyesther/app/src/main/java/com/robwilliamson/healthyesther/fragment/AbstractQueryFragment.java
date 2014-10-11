package com.robwilliamson.healthyesther.fragment;

import android.support.v4.app.Fragment;

import com.robwilliamson.db.use.Query;

public abstract class AbstractQueryFragment extends Fragment {
    public abstract Query getOnResumeQuery();
}
