package com.robwilliamson.healthyesther.edit;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import test.ActivityTestContext;
import test.view.EditEventFragmentAccessor;
import test.view.EditMealFragmentAccessor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MealEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String MEAL_NAME = "Bacon";
    private static final String EDITED_MEAL_NAME = "Christmas Pud";

    private ActivityTestContext<TestableMealEventActivity> mContext;

    private EditEventFragmentAccessor mEventAccessor;
    private EditMealFragmentAccessor mMealAccessor;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableMealEventActivity.class);

        mEventAccessor = new EditEventFragmentAccessor(mContext);
        mMealAccessor = new EditMealFragmentAccessor(mContext);
    }

    @After
    public void teardown() {
        mContext.close();
    }

    @Test
    public void getEditFragmentsWithTrue_returnsAListOfNewFragmentTagPairs() {
        List<Pair<EditFragment, String>> list = mContext.getActivity().getEditFragments(true);

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
        mContext.getActivityController().create().start().resume().visible();

        List<Pair<EditFragment, String>> list = mContext.getActivity().getEditFragments(false);

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

    @Test
    public void withExistingMeal_populatesAutocompleteList() {
        withExistingMeal();

        assertThat(Utils.checkNotNull(mMealAccessor.getNameTextView()).getAdapter().getCount(), is(1));
    }

    @Test
    public void withExistingMeal_populatesAutocompleteListWithMealName() {
        withExistingMeal();

        assertThat(((String)Utils.checkNotNull(mMealAccessor.getNameTextView()).getAdapter().getItem(0)),
                is(MEAL_NAME));
    }

    private void withExistingMeal() {
        Database db = HealthDbHelper.getDatabase();

        try (Transaction transaction = db.getTransaction()) {
            MealTable.Row meal = new MealTable.Row(MEAL_NAME);
            meal.applyTo(transaction);
            transaction.commit();
            Robolectric.flushBackgroundThreadScheduler();
        }

        mContext.getActivityController().setup();
    }

    private void openedWithAnExistingEventFromAnIntent() {
        Database db = HealthDbHelper.getDatabase();
        EventTable.Row event;
        MealEventTable.Row mealEvent;

        try (Transaction transaction = db.getTransaction()) {
            event = new EventTable.Row(
                    EventTypeTable.MEAL.getId(),
                    mContext.getNow(),
                    mContext.getNow(),
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
        mContext.getActivityController().withIntent(intent).create().start().resume();
    }

    private static class TestableMealEventActivity extends MealEventActivity {
        public boolean finishCalled = false;

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
