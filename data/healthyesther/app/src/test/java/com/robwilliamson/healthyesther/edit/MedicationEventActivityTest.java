/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.edit;

import android.content.Intent;
import android.os.Bundle;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import test.ActivityTestContext;
import test.view.EditEventFragmentAccessor;
import test.view.EditMedicationFragmentAccessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MedicationEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String MEDICATION_NAME = "Paracetamol";
    private static final String EDITED_MEDICATION_NAME = "Erythromycin";
    private static final String EDITED_EVENT_NAME = "New Event Name";

    private ActivityTestContext<TestableMedicationEventActivity> mContext;

    private EditEventFragmentAccessor mEventFragmentAccessor;
    private EditMedicationFragmentAccessor mMedicationFragmentAccessor;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableMedicationEventActivity.class);

        mEventFragmentAccessor = new EditEventFragmentAccessor(mContext);
        mMedicationFragmentAccessor = new EditMedicationFragmentAccessor(mContext);
    }

    @After
    public void teardown() {
        mContext.close();
    }

    @Test
    public void whenANewMedicationIsAdded_createsANewMedication() {
        aNewMedicationIsAdded();

        Database db = HealthDbHelper.getDatabase();

        MedicationTable.Row row = HealthDatabase.MEDICATION_TABLE.select0Or1(db, WhereContains.any());
        assertThat(row, is(notNullValue()));
    }

    @Test
    public void whenANewMedicationIsAdded_medicationNameIsCorrect() {
        aNewMedicationIsAdded();

        Database db = HealthDbHelper.getDatabase();

        MedicationTable.Row row = HealthDatabase.MEDICATION_TABLE.select0Or1(db, WhereContains.any());
        //noinspection ConstantConditions
        assertThat(row.getName(), is(MEDICATION_NAME));
    }

    @Test
    public void whenANewMedicationIsAdded_createsANewEvent() {
        aNewMedicationIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select0Or1(db, WhereContains.any());
        assertThat(row, is(notNullValue()));
    }

    @Test
    public void whenANewMedicationIsAdded_eventNameIsCorrect() {
        aNewMedicationIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select0Or1(db, WhereContains.any());
        //noinspection ConstantConditions
        assertThat(row.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenANewMedicationIsAdded_medicationEventRefersToEvent() {
        aNewMedicationIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row eventRow = HealthDatabase.EVENT_TABLE.select0Or1(db, WhereContains.any());

        MedicationEventTable.Row medEventRow = HealthDatabase.MEDICATION_EVENT_TABLE.select0Or1(db, WhereContains.any());

        //noinspection ConstantConditions
        assertThat(eventRow.getConcretePrimaryKey(), equalTo(medEventRow.getConcretePrimaryKey().getEventId()));
    }

    @Test
    public void whenANewMedicationIsAdded_medicationEventRefersToMedication() {
        aNewMedicationIsAdded();

        Database db = HealthDbHelper.getDatabase();

        MedicationTable.Row medRow = HealthDatabase.MEDICATION_TABLE.select0Or1(db, WhereContains.any());

        MedicationEventTable.Row medEventRow = HealthDatabase.MEDICATION_EVENT_TABLE.select0Or1(db, WhereContains.any());

        //noinspection ConstantConditions
        assertThat(medRow.getConcretePrimaryKey(), equalTo(medEventRow.getConcretePrimaryKey().getMedicationId()));
    }

    @Test
    public void whenOpenedWithAnExistingMedicationEvent_textViewShowsEventText() {
        openedWithAnExistingMedicationEvent();

        assertThat(mEventFragmentAccessor.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenOpenedWithAnExistingMedicationEvent_textViewShowsMedicationText() {
        openedWithAnExistingMedicationEvent();

        assertThat(mMedicationFragmentAccessor.getName(), is(MEDICATION_NAME));
    }

    @Test
    public void whenAnExistingMedicationNameIsEdited_aNewMedicationTypeIsAdded() {
        anExistingMedicationNameIsEdited();

        Database db = HealthDbHelper.getDatabase();

        MedicationTable.Row[] rows = HealthDatabase.MEDICATION_TABLE.select(db, WhereContains.any());

        assertThat(rows.length, is(2));
    }

    @Test
    public void whenAnExistingMedicationNameIsEdited_noNewEventIsAdded() {
        anExistingMedicationNameIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row[] rows = HealthDatabase.EVENT_TABLE.select(db, WhereContains.any());

        assertThat(rows.length, is(1));
    }

    @Test
    public void whenAnExistingMedicationNameIsEdited_noNewMedicationEventIsAdded() {
        anExistingMedicationNameIsEdited();

        Database db = HealthDbHelper.getDatabase();

        MedicationEventTable.Row[] rows = HealthDatabase.MEDICATION_EVENT_TABLE.select(db, WhereContains.any());

        assertThat(rows.length, is(1));
    }

    @Test
    public void whenOpenedWithAnExistingMedicationEventAndEditedAndReopened_textViewShowsEditedEventText() {
        openedWithAnExistingMedicationEventAndEditedAndReopened();

        assertThat(mEventFragmentAccessor.getName(), is(EDITED_EVENT_NAME));
    }

    private void openedWithAnExistingMedicationEventAndEditedAndReopened() {
        anExistingMedicationNameIsEdited();

        Intent intent = mContext.getActivity().getIntent();
        Bundle bundle = new Bundle();
        mContext.getActivityController().pause().saveInstanceState(bundle).stop().destroy();
        mContext.reset(intent);

        mContext.getActivityController().setup(bundle);
    }

    private void anExistingMedicationNameIsEdited() {
        openedWithAnExistingMedicationEvent();

        mMedicationFragmentAccessor.setName(EDITED_MEDICATION_NAME);
        mEventFragmentAccessor.setName(EDITED_EVENT_NAME);
        mContext.pressOk();
    }

    private MedicationEventTable.Row openedWithAnExistingMedicationEvent() {
        Database db = HealthDbHelper.getDatabase();
        EventTable.Row event;
        MedicationEventTable.Row medEvent;

        try (Transaction transaction = db.getTransaction()) {
            event = new EventTable.Row(
                    EventTypeTable.MEDICATION.getId(),
                    mContext.getNow(),
                    mContext.getNow(),
                    null,
                    EVENT_NAME);
            MedicationTable.Row med = new MedicationTable.Row(MEDICATION_NAME);

            medEvent = new MedicationEventTable.Row(event, med);
            medEvent.applyTo(transaction);
            transaction.commit();
            Robolectric.flushBackgroundThreadScheduler();
        }

        Intent intent = new Intent();
        intent.putExtra(HealthDatabase.EVENT_TABLE.getName(), event);
        mContext.reset(intent);
        mContext.getActivityController().setup();
        return medEvent;
    }

    private void aNewMedicationIsAdded() {
        mContext.getActivityController().setup();
        mMedicationFragmentAccessor.setName(MEDICATION_NAME);
        mEventFragmentAccessor.setName(EVENT_NAME);
        mContext.pressOk();
    }

    private static class TestableMedicationEventActivity extends MedicationEventActivity {
        private volatile boolean mBusy;

        @Override
        protected boolean isBusy() {
            return mBusy;
        }

        @Override
        protected void setBusy(boolean busy) {
            mBusy = busy;
        }
    }
}
