package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.MedicationEvent;
import com.robwilliamson.db.definition.MedicationName;
import com.robwilliamson.db.use.GetAllMedicationNamesQuery;
import com.robwilliamson.db.use.GetAllMedicationsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMedicationFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class Medication extends AbstractAddActivity
        implements EditMedicationFragment.Watcher, EditEventFragment.Watcher {
    private final static String MEDICATION_TAG = "medication";
    private final static String EVENT_TAG = "event";

    private final HashMap<String, Long> mSuggestionIds = new HashMap<String, Long>();

    public Medication() {
        super(true);
    }

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
    protected Query getOnResumeQuery() {
        return new GetAllMedicationsQuery() {
            @Override
            public void postQueryProcessing(Cursor cursor) {
                mSuggestionIds.putAll(Utils.Db.cursorToSuggestionList(cursor,
                        com.robwilliamson.db.definition.Medication.NAME,
                        com.robwilliamson.db.definition.Medication._ID));
                getMoreNames();
            }

            @Override
            public void onQueryComplete(Cursor cursor) {
            }

            @Override
            public void onQueryFailed(Throwable error) {
                Toast.makeText(Medication.this, getText(R.string.could_not_get_autocomplete_text_for_medication), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onFragmentUpdate(EditMedicationFragment fragment) {
        invalidateOptionsMenu();

        if (getEventFragment().getUserEditedEventName()) {
            return;
        }

        if (getEventFragment().getName().isEmpty()) {
            getEventFragment().setUserEditedEventName(false);
        }

        getEventFragment().setName(fragment.getName());
    }

    @Override
    public void onFragmentUpdate(EditEventFragment fragment) {
        invalidateOptionsMenu();
    }

    private void getMoreNames() {
        query(new GetAllMedicationNamesQuery() {
            @Override
            public void postQueryProcessing(Cursor cursor) {
                mSuggestionIds.putAll(Utils.Db.cursorToSuggestionList(cursor,
                        MedicationName.NAME,
                        MedicationName.MEDICATION_ID));
            }

            @Override
            public void onQueryComplete(Cursor cursor) {
                getMedicationFragment().setSuggestionIds(mSuggestionIds);
            }

            @Override
            public void onQueryFailed(Throwable error) {
                Toast.makeText(Medication.this, getText(R.string.could_not_get_autocomplete_text_for_medication), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EditMedicationFragment getMedicationFragment() {
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeFragment(getSupportFragmentManager(), MEDICATION_TAG);
    }

    private EditEventFragment getEventFragment() {
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeFragment(getSupportFragmentManager(), EVENT_TAG);
    }
}
