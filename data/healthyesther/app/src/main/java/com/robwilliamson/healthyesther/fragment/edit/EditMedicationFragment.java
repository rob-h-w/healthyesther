package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Medication;
import com.robwilliamson.healthyesther.db.definition.MedicationName;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.GetAllMedicationNamesQuery;
import com.robwilliamson.healthyesther.db.use.GetAllMedicationsQuery;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.R;

import java.util.HashMap;

public class EditMedicationFragment extends SuggestionEditFragment<EditMedicationFragment.Watcher> {

    public interface Watcher {
        void onFragmentUpdate(EditMedicationFragment fragment);
        void onQueryFailed(EditMedicationFragment fragment, Throwable error);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_medication;
    }

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new GetAllMedicationsQuery() {
                    HashMap<String, Long> mSuggestionIds;
                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mSuggestionIds = new HashMap<String, Long>();
                        mSuggestionIds.putAll(Utils.Db.cursorToSuggestionList(cursor,
                                com.robwilliamson.healthyesther.db.definition.Medication.NAME,
                                com.robwilliamson.healthyesther.db.definition.Medication._ID));
                    }

                    @Override
                    public void onQueryComplete(Cursor cursor) {
                        EditMedicationFragment.this.setSuggestionIds(mSuggestionIds);
                    }

                    @Override
                    public void onQueryFailed(final Throwable error) {
                        callWatcher(new WatcherCaller<Watcher>() {
                            @Override
                            public void call(Watcher watcher) {
                                watcher.onQueryFailed(EditMedicationFragment.this, error);
                            }
                        });
                    }
                },
                new GetAllMedicationNamesQuery() {
                    HashMap<String, Long> mSuggestionIds;
                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mSuggestionIds = new HashMap<String, Long>();
                        mSuggestionIds.putAll(Utils.Db.cursorToSuggestionList(cursor,
                                MedicationName.NAME,
                                MedicationName.MEDICATION_ID));
                    }

                    @Override
                    public void onQueryComplete(Cursor cursor) {
                        EditMedicationFragment.this.appendSuggestionIds(mSuggestionIds);
                    }

                    @Override
                    public void onQueryFailed(final Throwable error) {
                        callWatcher(new WatcherCaller<Watcher>() {
                            @Override
                            public void call(Watcher watcher) {
                                watcher.onQueryFailed(EditMedicationFragment.this, error);
                            }
                        });
                    }
                }
        };
    }

    @Override
    public Modification getModification() {
        String name = getName();

        Long id = getSuggestionId(name);
        if (id != null) {
            return new Medication.Modification(id);
        }

        return new Medication.Modification(name);
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

    @Override
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.medication_name);
    }
}
