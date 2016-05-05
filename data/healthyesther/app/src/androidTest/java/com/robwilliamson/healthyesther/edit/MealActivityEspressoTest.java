/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.edit;

import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import com.robwilliamson.healthyesther.test.EditAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MealEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class MealActivityEspressoTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    private static final DateTime when;
    private static final String MEAL_NAME = "Bacon";
    private static final String EVENT_NAME = "Breakfast";

    static {
        when = DateTimeConverter.now();
    }

    @Before
    public void setUp() throws Exception {
        HomeActivityAccessor.setShowNavigationDrawer(false);

        Utils.Db.TestData.cleanOldData();

        HomeActivityAccessor.AddMode.start();
    }

    @Test
    public void testAddNewMeal() {
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                onView(MealEventActivityAccessor.dishTitle()).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
                onView(MealEventActivityAccessor.dishName()).check(matches(withHint(from(R.string.descriptive_name_for_the_dish))));
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });

        final String text = "A big old trout";
        onView(MealEventActivityAccessor.dishName()).perform(typeText(text));

        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                onView(MealEventActivityAccessor.dishName()).check(matches(withText(text)));
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });

        // Ensure all busy activity has settled down:
        SystemClock.sleep(1000);
        onView(EditAccessor.ok()).check(matches(isDisplayed()));

        onView(EditAccessor.ok()).perform(click());

        checkDatabaseCorrectnessForName(text, 1);
    }

    @Test
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
            onView(EditAccessor.ok()).perform(click());
            onView(HomeActivityAccessor.AddMode.mealScoreButton()).check(matches(isDisplayed()));
            checkDatabaseCorrectnessForName(meal, 1);
        }

        Database db = HealthDbHelper.getDatabase();
        MealTable.Row[] meals = DatabaseAccessor.MEAL_TABLE.select(db, WhereContains.any());

        assertThat(meals.length, is(5));
    }

    @Test
    public void test_addMealName_updatesEventName() {
        String mealName = "A Meal";
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        onView(MealEventActivityAccessor.dishName()).perform(typeText(mealName));

        onView(EditAccessor.eventEditText()).check(matches(withText(mealName)));
    }

    @Test
    public void test_emptyName_cannotCommit() {
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        onView(EditAccessor.ok()).check(doesNotExist());
    }

    @Test
    public void test_editExisting_orientation() {
        setupMealEvent();
        HomeActivityAccessor.EditMode.start();
        onView(HomeActivityAccessor.EditMode.eventList()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                onView(EditAccessor.eventEditText()).check(matches(withText(MEAL_NAME)));
                onView(MealEventActivityAccessor.dishName()).check(matches(withText(MEAL_NAME)));
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void test_editExisting_commitsUpdate() {
        setupMealEvent();
        HomeActivityAccessor.EditMode.start();
        onView(HomeActivityAccessor.EditMode.eventList()).perform(click());

        ViewInteraction eventEditText = onView(EditAccessor.eventEditText());
        eventEditText.perform(clearText());
        eventEditText.perform(typeText(EVENT_NAME));
        onView(EditAccessor.ok()).perform(click());

        EventTable.Row event = DatabaseAccessor.EVENT_TABLE.select0Or1(HealthDbHelper.getDatabase(), WhereContains.any());
        if (event == null) {
            fail("No event in the database.");
        }

        assertThat(event.getName(), is(EVENT_NAME));
    }

    @Test
    public void test_editExistingModifyMealName_commitsUpdate() {
        final String NEW_FOOD = "Egg";
        setupMealEvent();
        HomeActivityAccessor.EditMode.start();
        onView(HomeActivityAccessor.EditMode.eventList()).perform(click());

        ViewInteraction mealEditText = onView(MealEventActivityAccessor.dishName());
        mealEditText.perform(clearText());
        mealEditText.perform(typeText(NEW_FOOD));
        onView(EditAccessor.ok()).perform(click());

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
        EventTable.Row event;
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
