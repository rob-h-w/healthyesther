package com.robwilliamson.healthyesther.edit;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.MedicationEvent;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMedicationFragment;

import java.util.ArrayList;

public class MedicationActivity extends AbstractEditActivity
        implements EditMedicationFragment.Watcher, EditEventFragment.Watcher {
    private final static String MEDICATION_TAG = "medication";
    private final static String EVENT_TAG = "event";

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(2);
        EditFragment meal;
        EditFragment event;
        if (create) {
            meal = new EditMedicationFragment();
            event = new EditEventFragment();
        } else {
            meal = getMedicationFragment();
            event = getEventFragment();
        }

        list.add(new Pair<>(meal, MEDICATION_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
        com.robwilliamson.healthyesther.db.definition.Medication.Modification medication = (com.robwilliamson.healthyesther.db.definition.Medication.Modification) getMedicationFragment().getModification();
        Event.Modification event = (Event.Modification) getEventFragment().getModification();
        MedicationEvent.Modification medicationEvent = new MedicationEvent.Modification(medication, event);
        medicationEvent.modify(db);
    }

    @Override
    protected int getModifyFailedStringId() {
        return R.string.could_not_insert_medication_event;
    }

    @Override
    public void onFragmentUpdate(EditMedicationFragment fragment) {
        getEventFragment().suggestEventName(fragment.getName());
        invalidateOptionsMenu();
    }

    @Override
    public void onQueryFailed(EditMedicationFragment fragment, Throwable error) {
        Toast.makeText(this, getText(R.string.could_not_get_autocomplete_text_for_medication), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentUpdate(EditEventFragment fragment) {
        invalidateOptionsMenu();
    }

    private EditMedicationFragment getMedicationFragment() {
        return getFragment(MEDICATION_TAG, EditMedicationFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    @Override
    public QueryUser[] getQueryUsers() {
        return new QueryUser[] {
                getMedicationFragment()
        };
    }
}
