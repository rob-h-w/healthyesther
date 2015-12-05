package com.robwilliamson.healthyesther.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

public class AddValueFragment extends BaseFragment {
    private View.OnClickListener mClickListener = null;

    public AddValueFragment() {
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_add_value;
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
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.add_value_text, TextView.class);
    }

    private FrameLayout getLayout() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.add_value_layout, FrameLayout.class);
    }
}
