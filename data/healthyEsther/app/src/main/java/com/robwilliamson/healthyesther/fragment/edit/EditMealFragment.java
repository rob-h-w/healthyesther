package com.robwilliamson.healthyesther.fragment.edit;



import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleCursorAdapter;

import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.healthyesther.R;

import java.util.ArrayList;

public class EditMealFragment extends Fragment {
    private long[] mMealIds;

    public EditMealFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_meal, container, false);
    }

    public void setCursor(Cursor meals) {
        String[] suggestions = new String [meals.getCount()];
        mMealIds = new long[meals.getCount()];

        int i = 0;
        if (meals.moveToFirst()) {
            do {
                suggestions[i] = meals.getString(meals.getColumnIndex(Meal.NAME));
                mMealIds[i] = meals.getLong(meals.getColumnIndex(Meal._ID));
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
}
