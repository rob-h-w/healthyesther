package com.robwilliamson.healthyesther.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.robwilliamson.healthyesther.R;

public class AddEventFragment extends Fragment {
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_add_event, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_event) {
            Toast.makeText(getActivity(), "TODO: implement add an entry.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_add_event);
        if (item != null) {
            item.setEnabled(AddPossible());
        }
    }

    @Override
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
                            getActivity().invalidateOptionsMenu();
                        }
                    }
                });

                return true;
            }
        });
    }

    public boolean AddPossible() {
        return getCheckedRadioButtonId() != -1;
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
