package com.robwilliamson.healthyesther.fragment.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;

import javax.annotation.Nonnull;

public class EditMedicationFragment extends SuggestionEditFragment<MedicationTable.Row> {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_medication;
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
                updateAttachedActivity();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getNameView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAttachedActivity();
            }
        });
    }

    @Override
    public boolean validate() {
        return true; // TODO: Implement in the generated code.
    }

    @Override
    protected MedicationTable.Row createRow() {
        return new MedicationTable.Row(getName());
    }

    @Nonnull
    public String getName() {
        if (getNameView() != null) {
            return getNameView().getText().toString();
        }

        return "";
    }

    @Override
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.medication_name, AutoCompleteTextView.class);
    }
}
