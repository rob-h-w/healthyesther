package com.robwilliamson.healthyesther.edit;

import android.text.Editable;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MedicationEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String MEDICATION_NAME = "Paracetamol";
    private static final String EDITED_MEDICATION_NAME = "Erythromycin";

    private ActivityController<TestableMedicationEventActivity> mActivityController;

    private TestableMedicationEventActivity mActivity;
    private DateTime mNow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Utils.Db.TestData.cleanOldData();

        mNow = DateTimeConverter.now();

        mActivityController = Robolectric.buildActivity(TestableMedicationEventActivity.class);

        mActivity = mActivityController.get();
    }

    @After
    public void teardown() {
        Utils.Db.TestData.cleanOldData();
        HealthDbHelper.closeDb();
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
    public void whenAnExistingMedicationNameIsEdited_aNewMedicationTypeIsAdded() {
        anExistingMedicationNameIsEdited();
    }

    private void anExistingMedicationNameIsEdited() {
        Database db = HealthDbHelper.getDatabase();

        try(Transaction transaction = db.getTransaction()) {
            EventTable.Row event = new EventTable.Row(
                    EventTypeTable.MEDICATION.getId(),
                    mNow,
                    mNow,
                    null,
                    EVENT_NAME);

            MedicationTable.Row med = new MedicationTable.Row(MEDICATION_NAME);
            MedicationEventTable.Row medEvent = new MedicationEventTable.Row(event, med);
            medEvent.applyTo(transaction);
        }
    }

    private void aNewMedicationIsAdded() {
        mActivity = mActivityController.create().start().resume().get();
        setMedication(MEDICATION_NAME);
        setEvent(EVENT_NAME);
        mActivity.pressOk();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
    }

    private void setMedication(@Nonnull String name) {
        setText(name, R.id.medication_name);
    }

    private void setEvent(@Nullable String name) {
        setText(name, R.id.edit_event_name);
    }

    private void setText(@Nullable String text, int id) {
        Editable editable = ((AutoCompleteTextView) mActivity.findViewById(id)).getEditableText();
        editable.clear();
        editable.append(text);
    }

    private static class TestableMedicationEventActivity extends MedicationEventActivity {
        public void pressOk() {
            MenuItem item = Mockito.mock(MenuItem.class);
            doReturn(R.id.action_modify).when(item).getItemId();
            onOptionsItemSelected(item);
        }

        @Override
        protected void setBusy(boolean busy) {
            // TODO: Find a way to avoid the countdown latch lock.
            //super.setBusy(busy);
        }
    }
}
