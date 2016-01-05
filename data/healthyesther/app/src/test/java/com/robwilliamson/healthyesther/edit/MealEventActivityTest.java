package com.robwilliamson.healthyesther.edit;

import android.content.Intent;
import android.util.Pair;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

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

import java.util.ArrayList;
import java.util.List;

import test.view.EditEventFragmentAccessor;
import test.view.EditMealFragmentAccessor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MealEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String MEAL_NAME = "Bacon";
    private static final String EDITED_MEAL_NAME = "Christmas Pud";

    private ActivityController<TestableMealEventActivity> mActivityController;

    private TestableMealEventActivity mActivity;
    private DateTime mNow;

    private EditEventFragmentAccessor mEventAccessor;
    private EditMealFragmentAccessor mMealAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Utils.Db.TestData.cleanOldData();

        mNow = DateTimeConverter.now();

        mActivityController = Robolectric.buildActivity(TestableMealEventActivity.class);
        mActivity = mActivityController.get();

        mEventAccessor = new EditEventFragmentAccessor(mActivity);
        mMealAccessor = new EditMealFragmentAccessor(mActivity);
    }

    @After
    public void teardown() {
        Utils.Db.TestData.cleanOldData();
        HealthDbHelper.closeDb();
    }

    @Test
    public void getEditFragmentsWithTrue_returnsAListOfNewFragmentTagPairs() {
        List<Pair<EditFragment, String>> list = mActivity.getEditFragments(true);

        List<EditFragment> fragments = new ArrayList<>();
        for (Pair<EditFragment, String> pair : list) {
            fragments.add(pair.first);
        }

        assertThat(fragments.size(), is(2));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditMealFragment.class)));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditEventFragment.class)));
    }

    @Test
    public void getEditFragmentsWhenShownWithFalse_returnsAListOfExistingFragmentTagPairs() {
        mActivityController.create().start().resume().visible();

        List<Pair<EditFragment, String>> list = mActivity.getEditFragments(false);

        List<EditFragment> fragments = new ArrayList<>();
        for (Pair<EditFragment, String> pair : list) {
            fragments.add(pair.first);
        }

        assertThat(fragments.size(), is(2));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditMealFragment.class)));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditEventFragment.class)));
    }

    @Test
    public void whenOpenedWithAnExistingEventFromAnIntent_setsEventNameOnEventEditFragment() {
        openedWithAnExistingEventFromAnIntent();

        assertThat(mEventAccessor.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenOpenedWithAnExistingEventFromAnIntent_setsMealOnMealEditFragment() {
        openedWithAnExistingEventFromAnIntent();

        assertThat(mMealAccessor.getName(), is(MEAL_NAME));
    }

    private void openedWithAnExistingEventFromAnIntent() {
        Database db = HealthDbHelper.getDatabase();
        EventTable.Row event;
        MealEventTable.Row mealEvent;

        try (Transaction transaction = db.getTransaction()) {
            event = new EventTable.Row(
                    EventTypeTable.MEAL.getId(),
                    mNow,
                    mNow,
                    null,
                    EVENT_NAME);
            MealTable.Row meal = new MealTable.Row(MEAL_NAME);

            mealEvent = new MealEventTable.Row(event, meal, null, null);
            mealEvent.applyTo(transaction);
            transaction.commit();
            Robolectric.flushBackgroundThreadScheduler();
        }

        Intent intent = new Intent();
        intent.putExtra(HealthDatabase.EVENT_TABLE.getName(), event);
        mActivity = mActivityController.withIntent(intent).create().start().resume().get();
    }

    private static class TestableMealEventActivity extends MealEventActivity {
        public boolean finishCalled = false;

        public void pressOk() {
            MenuItem item = Mockito.mock(MenuItem.class);
            doReturn(R.id.action_modify).when(item).getItemId();
            onOptionsItemSelected(item);
        }

        @Override
        public void finish() {
            finishCalled = true;
            super.finish();
        }

        @Override
        protected void setBusy(boolean busy) {
            // TODO: Find a way to avoid the countdown latch lock.
            //super.setBusy(busy);
        }
    }
}
