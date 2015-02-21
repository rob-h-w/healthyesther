package com.robwilliamson.healthyesther.fragment.edit;


import android.database.Cursor;
import android.os.Bundle;
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
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.GetAllMealsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;

import java.util.HashMap;
import java.util.Set;

public class EditMealFragment extends EditFragment<EditMealFragment.Watcher> {
    private HashMap<String, Long> mSuggestionIds;

    public interface Watcher {
        void onFragmentUpdate(EditMealFragment fragment);
        void onQueryFailed(EditMealFragment fragment, Throwable error);
    }

    public EditMealFragment() {}

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new GetAllMealsQuery() {
                    HashMap<String, Long> mSuggestionIds;

                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mSuggestionIds = com.robwilliamson.db.Utils.Db.cursorToSuggestionList(cursor,
                                com.robwilliamson.db.definition.Meal.NAME,
                                com.robwilliamson.db.definition.Meal._ID);
                    }

                    @Override
                    public void onQueryComplete(final Cursor cursor) {
                        EditMealFragment.this.setSuggestionIds(mSuggestionIds);
                    }

                    @Override
                    public void onQueryFailed(final Throwable error) {
                        callWatcher(new WatcherCaller<Watcher>() {
                            @Override
                            public void call(Watcher watcher) {
                                watcher.onQueryFailed(EditMealFragment.this, error);
                            }
                        });
                    }
                }
        };
    }

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
    public Modification getModification() {
        String name = getName();

        if (mSuggestionIds.containsKey(name)) {
            return new Meal.Modification(mSuggestionIds.get(name));
        }

        return new Meal.Modification(name);
    }

    @Override
    public boolean validate() {
        return Contract.getInstance().MEAL.validateName(getName());
    }

    @Override
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    public void setSuggestionIds(HashMap<String, Long> suggestionIds) {
        mSuggestionIds = suggestionIds;

        Set<String> set = mSuggestionIds.keySet();
        String [] suggestions = new String[set.size()];
        set.toArray(suggestions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions);
        getNameView().setAdapter(adapter);
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    private AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.meal_name);
    }
}
