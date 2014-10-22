package com.robwilliamson.healthyesther.add;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.MedicationEvent;
import com.robwilliamson.db.use.QueryUser;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMedicationFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MedicationActivity extends AbstractAddActivity
        implements EditMedicationFragment.Watcher, EditEventFragment.Watcher {
    private final static String MEDICATION_TAG = "medication";
    private final static String EVENT_TAG = "event";

    private final HashMap<String, Long> mSuggestionIds = new HashMap<String, Long>();

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<Pair<EditFragment, String>>(2);
        EditFragment meal = null;
        EditFragment event = null;
        if (create) {
            meal = new EditMedicationFragment();
            event = new EditEventFragment();
        } else {
            meal = getMedicationFragment();
            event = getEventFragment();
        }

        list.add(new Pair<EditFragment, String>(meal, MEDICATION_TAG));
        list.add(new Pair<EditFragment, String>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
        com.robwilliamson.db.definition.Medication.Modification medication = (com.robwilliamson.db.definition.Medication.Modification) getMedicationFragment().getModification();
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
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeFragment(getSupportFragmentManager(), MEDICATION_TAG);
    }

    private EditEventFragment getEventFragment() {
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeFragment(getSupportFragmentManager(), EVENT_TAG);
    }

    @Override
    protected QueryUser[] getOnResumeQueryUsers() {
        return new QueryUser[] {
                getMedicationFragment()
        };
    }
}
