package com.robwilliamson.healthyesther;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class HomeFragment extends Fragment {
    private HomeFragmentCallbacks mCallbacks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (HomeFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement HomeFragmentCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_home, container, false);
    }

    public boolean AddPossible() {
        return getCheckedRadioButtonId() != -1;
    }

    private void enableAdd() {
        enableAdd(true);
    }

    private void enableAdd(boolean enable){
        if (mCallbacks != null) {
            mCallbacks.enableAdd(enable);
        }
    }

    public void onResume() {
        super.onResume();

        forEachRadioButton(new RadioButtonHandler() {
            @Override
            public boolean handleRadioButton(RadioButton button) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton button = (RadioButton) view;

                        if (button.isChecked()) {
                            enableAdd();
                        }
                    }
                });

                return true;
            }
        });
    }

    public interface HomeFragmentCallbacks {
        public void enableAdd(boolean enable);
    }

    private int getCheckedRadioButtonId() {
        final int[] id = new int[] { -1 };
        forEachRadioButton(new RadioButtonHandler() {
            @Override
            public boolean handleRadioButton(RadioButton button) {
                if (button.isChecked()) {
                    id[0] = button.getId();
                    return false;
                }

                return true;
            }
        });
        return id[0];
    }

    private interface RadioButtonHandler {
        /**
         * @param button
         * @return true to loop through the other buttons, false to exit with this one.
         */
        public boolean handleRadioButton(RadioButton button);
    }

    private void forEachRadioButton(RadioButtonHandler handler) {
        RadioGroup group = (RadioGroup) getView().findViewById(R.id.createEventRadioGroup);
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = group.getChildAt(i);
            if (!(view instanceof RadioButton)) {
                continue;
            }

            if (!handler.handleRadioButton((RadioButton)view)) {
                break;
            }
        }
    }
}
