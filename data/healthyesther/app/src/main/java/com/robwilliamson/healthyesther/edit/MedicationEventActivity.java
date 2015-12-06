package com.robwilliamson.healthyesther.edit;

import android.util.Pair;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMedicationFragment;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MedicationEventActivity extends AbstractEditEventActivity
        implements BaseFragment.Watcher {
    private final static String MEDICATION_TAG = "medication";
    private final static String EVENT_TAG = "event";

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(2);
        EditFragment meal;
        EditFragment event;
        if (create) {
            meal = new EditMedicationFragment();
            DateTime now = DateTime.from(com.robwilliamson.healthyesther.db.Utils.Time.localNow());
            event = EditEventFragment.getInstance(
                    new EventTable.Row(
                            EventTypeTable.MEDICATION.getId(),
                            now,
                            now,
                            null,
                            null));
        } else {
            meal = getMedicationFragment();
            event = getEventFragment();
        }

        list.add(new Pair<>(meal, MEDICATION_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                EventTable.Row event = Utils.checkNotNull(getEventFragment().getRow());
                event.setTypeId(EventTypeTable.MEDICATION.getId());
                MedicationTable.Row medication = Utils.checkNotNull(getMedicationFragment().getRow());
                event.applyTo(transaction);
                medication.applyTo(transaction);
                MedicationEventTable.Row medicationEvent = DatabaseAccessor.MEDICATION_EVENT_TABLE.select0Or1(
                        database,
                        new MedicationEventTable.PrimaryKey(event.getNextPrimaryKey(), medication.getNextPrimaryKey()));

                if (medicationEvent == null) {
                    medicationEvent = new MedicationEventTable.Row(event.getNextPrimaryKey(), medication.getNextPrimaryKey());
                }

                medicationEvent.applyTo(transaction);

                finish();
            }
        };
    }

    private EditMedicationFragment getMedicationFragment() {
        return getFragment(MEDICATION_TAG, EditMedicationFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    @Override
    public void onFragmentUpdated(BaseFragment fragment) {
        if (fragment instanceof EditMedicationFragment) {
            EditMedicationFragment editMedicationFragment = (EditMedicationFragment) fragment;
            getEventFragment().suggestEventName(editMedicationFragment.getName());
        }

        invalidateOptionsMenu();
    }
}
