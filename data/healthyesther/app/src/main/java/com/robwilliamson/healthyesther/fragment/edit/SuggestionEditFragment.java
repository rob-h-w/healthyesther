package com.robwilliamson.healthyesther.fragment.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.db.includes.BaseRow;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Use with fragments that have an auto complete box with suggestions.
 */
public abstract class SuggestionEditFragment<R extends BaseRow> extends EditFragment<R> {
    private Map<String, Long> mSuggestionIds;

    @Override
    public void onResume() {
        super.onResume();

        getNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onNameChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getNameView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onNameClicked();
            }
        });
    }

    protected void onNameChanged() {
        updateAttachedActivity();
    }

    protected void onNameClicked() {
        updateAttachedActivity();
    }

    protected void setSuggestionIds(Map<String, Long> suggestionIds) {
        mSuggestionIds = suggestionIds;

        if (null == mSuggestionIds) {
            getNameView().setAdapter(null);
            return;
        }

        Set<String> set = mSuggestionIds.keySet();
        String[] suggestions = new String[set.size()];
        set.toArray(suggestions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions);

        AutoCompleteTextView nameView = getNameView();

        if (nameView != null) {
            getNameView().setAdapter(adapter);
        }
    }

    protected void appendSuggestionIds(HashMap<String, Long> suggestionIds) {
        if (null == suggestionIds) {
            return;
        }

        mSuggestionIds.putAll(suggestionIds);
        setSuggestionIds(mSuggestionIds);
    }

    protected Long getSuggestionId(String name) {
        return mSuggestionIds == null ? null : mSuggestionIds.get(name);
    }

    protected abstract AutoCompleteTextView getNameView();
}
