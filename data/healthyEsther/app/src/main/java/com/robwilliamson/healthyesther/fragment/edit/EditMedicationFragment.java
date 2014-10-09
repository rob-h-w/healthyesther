package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.db.definition.Medication;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.HashMap;
import java.util.Set;

public class EditMedicationFragment extends EditFragment<EditMedicationFragment.Watcher> {
    private HashMap<String, Long> mSuggestionIds;

    public void setSuggestionIds(HashMap<String,Long> suggestionIds) {
        mSuggestionIds = suggestionIds;

        Set<String> set = mSuggestionIds.keySet();
        String [] suggestions = new String[set.size()];
        set.toArray(suggestions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions);
        getNameView().setAdapter(adapter);
    }

    public interface Watcher {
        void onFragmentUpdate(EditMedicationFragment fragment);
    }

    @Override
    public Modification getModification() {
        String name = getName();

        if (mSuggestionIds.containsKey(name)) {
            return new Medication.Modification(mSuggestionIds.get(name));
        }

        return new Medication.Modification(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_edit_medication, container, false);
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
        return Medication.validateName(getName());
    }

    @Override
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    private AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.medication_name);
    }

    private <T extends View> T getTypeSafeView(int id) {
        return Utils.View.getTypeSafeView(getView(), id);
    }
}
