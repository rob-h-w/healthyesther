package com.robwilliamson.healthyesther.edit;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.test.EditEventAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MealEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import javax.annotation.Nonnull;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.robwilliamson.healthyesther.test.Strings.from;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;

public class MealActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final DateTime when;
    private static final String MEAL_NAME = "Bacon";
    private static final String EVENT_NAME = "Breakfast";

    static {
        when = DateTimeConverter.now();
    }

    public MealActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData();

        getActivity();

        HomeActivityAccessor.AddMode.start();
    }

    public void testAddNewMeal() {
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return MealActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(MealEventActivityAccessor.dishTitle()).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
                onView(MealEventActivityAccessor.dishName()).check(matches(withHint(from(R.string.descriptive_name_for_the_dish))));
            }
        });

        final String text = "A big old trout";
        onView(MealEventActivityAccessor.dishName()).perform(typeText(text));

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return MealActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(MealEventActivityAccessor.dishName()).check(matches(withText(text)));
            }
        });

        onView(EditEventAccessor.ok()).perform(click());

        checkDatabaseCorrectnessForName(text, 1);
    }

    public void testAddMultipleMeals() {
        String[] mealNames = new String[]{
                "trout",
                "cod",
                "trout",
                "perch",
                "jelly donut",
                "spam",
                "trout"
        };

        for (String meal : mealNames) {
            onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
            onView(MealEventActivityAccessor.dishName()).perform(typeText(meal));
            onView(EditEventAccessor.ok()).perform(click());
            onView(HomeActivityAccessor.AddMode.mealScoreButton()).check(matches(isDisplayed()));
            checkDatabaseCorrectnessForName(meal, 1);
        }

        Database db = HealthDbHelper.getDatabase();
        MealTable.Row[] meals = DatabaseAccessor.MEAL_TABLE.select(db, WhereContains.any());

        assertThat(meals.length, is(5));
    }

    public void test_addMealName_updatesEventName() {
        String mealName = "A Meal";
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        onView(MealEventActivityAccessor.dishName()).perform(typeText(mealName));

        onView(EditEventAccessor.eventEditText()).check(matches(withText(mealName)));
    }

    public void test_emptyName_cannotCommit() {
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        onView(EditEventAccessor.ok()).check(doesNotExist());
    }

    public void test_editExisting_orientation() {
        setupMealEvent();
        HomeActivityAccessor.EditMode.start();
        onView(HomeActivityAccessor.EditMode.eventList()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return MealActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(EditEventAccessor.eventEditText()).check(matches(withText(MEAL_NAME)));
                onView(MealEventActivityAccessor.dishName()).check(matches(withText(MEAL_NAME)));
            }
        });
    }

    public void test_editExisting_commitsUpdate() {
        setupMealEvent();
        HomeActivityAccessor.EditMode.start();
        onView(HomeActivityAccessor.EditMode.eventList()).perform(click());

        ViewInteraction eventEditText = onView(EditEventAccessor.eventEditText());
        eventEditText.perform(clearText());
        eventEditText.perform(typeText(EVENT_NAME));
        onView(EditEventAccessor.ok()).perform(click());

        EventTable.Row event = DatabaseAccessor.EVENT_TABLE.select0Or1(HealthDbHelper.getDatabase(), WhereContains.any());
        if (event == null) {
            fail("No event in the database.");
        }

        assertThat(event.getName(), is(EVENT_NAME));
    }

    public void test_editExistingModifyMealName_commitsUpdate() {
        final String NEW_FOOD = "Egg";
        setupMealEvent();
        HomeActivityAccessor.EditMode.start();
        onView(HomeActivityAccessor.EditMode.eventList()).perform(click());

        ViewInteraction mealEditText = onView(MealEventActivityAccessor.dishName());
        mealEditText.perform(clearText());
        mealEditText.perform(typeText(NEW_FOOD));
        onView(EditEventAccessor.ok()).perform(click());

        Database db = HealthDbHelper.getDatabase();
        EventTable.Row event = DatabaseAccessor.EVENT_TABLE.select0Or1(db, WhereContains.any());
        if (event == null) {
            fail("No event in the database.");
        }

        MealEventTable.Row mealEvent = DatabaseAccessor.MEAL_EVENT_TABLE.select0Or1(db, WhereContains.columnEqualling(MealEventTable.EVENT_ID, event.getConcretePrimaryKey().getId()));
        if (mealEvent == null) {
            fail("Meal event not found.");
        }

        MealTable.Row meal = DatabaseAccessor.MEAL_TABLE.select0Or1(db, mealEvent.getConcretePrimaryKey().getMealId());
        if (meal == null) {
            fail("Meal not found.");
        }

        assertThat(meal.getName(), is(NEW_FOOD));
    }

    private void setupMealEvent() {
        Database db = HealthDbHelper.getDatabase();
        EventTable.Row event = null;
        try (Transaction transaction = db.getTransaction()) {
            event = new EventTable.Row(EventTypeTable.MEAL.getId(), when, when, null, MEAL_NAME);
            event.applyTo(transaction);
            MealTable.Row meal = new MealTable.Row(MEAL_NAME);
            meal.applyTo(transaction);
            MealEventTable.Row mealEvent = new MealEventTable.Row(event.getNextPrimaryKey(), meal.getNextPrimaryKey(), null, null);
            mealEvent.applyTo(transaction);
            transaction.commit();
        }
    }

    private void checkDatabaseCorrectnessForName(@Nonnull final String name, int expectedCount) {
        // Ensure all busy activity has settled down:
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).check(matches(isDisplayed()));

        // Check the database contents.
        Database db = HealthDbHelper.getDatabase();
        MealTable.Row[] meals = DatabaseAccessor.MEAL_TABLE.select(db, new Where() {
            @Override
            public String getWhere() {
                return MealTable.NAME + " = \"" + name + "\"";
            }
        });

        assertThat(meals.length, is(expectedCount));

        final MealTable.Row meal = meals[0];
        final MealEventTable.Row[] mealEvents = DatabaseAccessor.MEAL_EVENT_TABLE.select(db, new Where() {
            @Override
            public String getWhere() {
                return MealEventTable.MEAL_ID + " = " + meal.getConcretePrimaryKey().getId();
            }
        });

        assertThat(mealEvents.length, is(greaterThanOrEqualTo(1)));

        EventTable.Row[] events = DatabaseAccessor.EVENT_TABLE.select(db, new Where() {
            @Override
            public String getWhere() {
                return EventTable._ID + " = " + mealEvents[0].getConcretePrimaryKey().getEventId().getId();
            }
        });

        assertThat(events.length, is(greaterThanOrEqualTo(1)));
        assertThat(events[0].getTypeId().getId(), is(EventTypeTable.MEAL.getId().getId()));
    }
}
