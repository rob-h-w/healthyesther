package com.robwilliamson.healthyesther.edit;

import android.os.Bundle;
import android.support.v4.util.Pair;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
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
import javax.annotation.Nullable;

import static com.robwilliamson.healthyesther.db.includes.WhereContains.and;
import static com.robwilliamson.healthyesther.db.includes.WhereContains.foreignKey;

public class MedicationEventActivity extends AbstractEditActivity
        implements BaseFragment.Watcher {
    private final static String MEDICATION_TAG = "medication";
    private final static String EVENT_TAG = "event";
    private final static String MEDICATION_EVENT = "medication event";

    @Nullable
    private MedicationEventTable.Row mMedEvent;

    @Nonnull
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(MEDICATION_EVENT, mMedEvent);
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                EventTable.Row event = Utils.checkNotNull(getEventFragment().getRow());
                EventTable.PrimaryKey oldEventKey = mMedEvent == null ? null : mMedEvent.getConcretePrimaryKey().getEventId();
                event.setTypeId(EventTypeTable.MEDICATION.getId());
                MedicationTable.Row medication = Utils.checkNotNull(getMedicationFragment().getRow());
                MedicationTable.PrimaryKey oldMedicationKey = mMedEvent == null ? null : mMedEvent.getConcretePrimaryKey().getMedicationId();

                if (oldEventKey != null && oldMedicationKey != null) {
                    mMedEvent = DatabaseAccessor.MEDICATION_EVENT_TABLE.select0Or1(database, and(
                            foreignKey(MedicationEventTable.EVENT_ID, oldEventKey.getId()),
                            foreignKey(MedicationEventTable.MEDICATION_ID, oldMedicationKey.getId())));
                }

                event.applyTo(transaction);
                medication.applyTo(transaction);

                if (mMedEvent == null) {
                    mMedEvent = new MedicationEventTable.Row(event.getNextPrimaryKey(), medication.getNextPrimaryKey());
                } else {
                    mMedEvent.setNextPrimaryKey(new MedicationEventTable.PrimaryKey(event.getNextPrimaryKey(), medication.getNextPrimaryKey()));
                }

                mMedEvent.applyTo(transaction);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
    }

    @Override
    protected void resumeFromIntentExtras(@Nonnull Bundle bundle) {
        final EventTable.Row event = Utils.checkNotNull((EventTable.Row) bundle.getSerializable(HealthDatabase.EVENT_TABLE.getName()));
        if (!event.getTypeId().equals(EventTypeTable.MEDICATION.getId())) {
            throw new EventTypeTable.BadEventTypeException(EventTypeTable.MEDICATION, event.getTypeId().getId());
        }

        getEventFragment().setRow(event);

        getExecutor().perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                mMedEvent = HealthDatabase.MEDICATION_EVENT_TABLE.select1(
                        database,
                        foreignKey(MedicationEventTable.EVENT_ID, event.getConcretePrimaryKey().getId()));

                final MedicationTable.Row med = HealthDatabase.MEDICATION_TABLE.select1(
                        database,
                        foreignKey(MedicationTable._ID, mMedEvent.getConcretePrimaryKey().getMedicationId().getId()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMedicationFragment().setRow(med);
                    }
                });
            }
        });
    }

    @Override
    protected void resumeFromSavedState(@Nonnull Bundle bundle) {
        mMedEvent = (MedicationEventTable.Row) bundle.getSerializable(MEDICATION_EVENT);
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
