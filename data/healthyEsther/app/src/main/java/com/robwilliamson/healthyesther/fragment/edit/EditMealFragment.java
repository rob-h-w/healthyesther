package com.robwilliamson.healthyesther.fragment.edit;



import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.healthyesther.R;

public class EditMealFragment extends EditFragment<EditMealFragment.Watcher> {
    public interface Watcher {
        void onFragmentUpdate(EditMealFragment fragment);
    }

    public EditMealFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_meal, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        getNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateWatcher();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getNameView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateWatcher();
            }
        });
    }

    @Override
    public boolean validate() {
        return Contract.getInstance().MEAL.validateName(getName());
    }

    @Override
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    public void setCursor(final Cursor meals) {
        String[] suggestions = new String [meals.getCount()];
        long[] mealIds;
        mealIds = new long[meals.getCount()];

        int i = 0;
        if (meals.moveToFirst()) {
            do {
                suggestions[i] = meals.getString(meals.getColumnIndex(Meal.NAME));
                mealIds[i] = meals.getLong(meals.getColumnIndex(Meal._ID));
                i++;
            } while(meals.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions);
        getNameView().setAdapter(adapter);
    }

    private AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.meal_name);
    }

    private <T extends View> T getTypeSafeView(int id) {
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeView(getView(), id);
    }

    public String getName() {
        return getNameView().getText().toString();
    }
}
