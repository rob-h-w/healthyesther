package com.robwilliamson.healthyesther.fragment.edit;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;
import java.util.Set;

/**
 * Use with fragments that have an auto complete box with suggestions.
 * @param <T> Watcher for this fragment.
 */
public abstract class SuggestionEditFragment<T> extends EditFragment<T> {
    private HashMap<String, Long> mSuggestionIds;

    protected void setSuggestionIds(HashMap<String, Long> suggestionIds) {
        mSuggestionIds = suggestionIds;

        if (null == mSuggestionIds) {
            getNameView().setAdapter(null);
            return;
        }

        Set<String> set = mSuggestionIds.keySet();
        String [] suggestions = new String[set.size()];
        set.toArray(suggestions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions);
        getNameView().setAdapter(adapter);
    }

    protected Long getSuggestionId(String name) {
        return mSuggestionIds == null ? null : mSuggestionIds.get(name);
    }

    protected abstract AutoCompleteTextView getNameView();
}
