package com.robwilliamson.healthyesther.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

public class AddValueFragment extends Fragment {
    private View.OnClickListener mClickListener = null;

    public AddValueFragment() {
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_value, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Ensure we don't send spurious signals.
        setOnClickListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        
        getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });
    }

    public String getTitle() {
        return getTextView().getText().toString();
    }

    public void setTitle(int stringId) {
        setTitle(getString(stringId));
    }

    public void setTitle(String title) {
        getTextView().setText(title);
    }

    private TextView getTextView() {
        return Utils.View.getTypeSafeView(getView(), R.id.add_value_text);
    }

    private FrameLayout getLayout() {
        return Utils.View.getTypeSafeView(getView(), R.id.add_value_layout);
    }
}
