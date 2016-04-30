package com.robwilliamson.healthyesther.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

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

    protected abstract int getFragmentLayout();
}
