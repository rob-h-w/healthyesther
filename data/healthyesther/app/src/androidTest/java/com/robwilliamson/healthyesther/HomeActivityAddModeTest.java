package com.robwilliamson.healthyesther;

import android.os.Environment;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.MealEvent;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.integration.DatabaseWrapperClass;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.test.EditEventAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MealEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.robwilliamson.healthyesther.test.Strings.from;
import static org.hamcrest.core.Is.is;

public class HomeActivityAddModeTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final String DROPBOX_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/Android/data/com.dropbox.android";
    private static final String DB_PATH = DROPBOX_PATH + "/files/scratch";

    public HomeActivityAddModeTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(
                getInstrumentation().getTargetContext()).getWritableDatabase());

        getActivity();

        HomeActivityAccessor.AddMode.start();
    }

    public void testAddModeContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityAddModeTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.AddMode.checkUnmodifiedContent();
            }
        });
    }

    public void testAddNewMeal() {
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityAddModeTest.this;
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
                return HomeActivityAddModeTest.this;
            }

            @Override
            public void checkContent() {
                onView(MealEventActivityAccessor.dishName()).check(matches(withText(text)));
            }
        });

        onView(EditEventAccessor.ok()).perform(click());

        DatabaseWrapperClass db = new DatabaseWrapperClass(HealthDbHelper.getInstance(
                getInstrumentation().getTargetContext()).getWritableDatabase());
        MealTable.Row[] meals = HealthDatabase.MEAL_TABLE.select(db, new Where() {
            @Override
            public String getWhere() {
                return MealTable.NAME + " = \"" + text + "\"";
            }
        });

        assertThat(meals.length, is(1));

        final MealTable.Row meal = meals[0];
        final MealEventTable.Row[] mealEvents = HealthDatabase.MEAL_EVENT_TABLE.select(db, new Where() {
            @Override
            public String getWhere() {
                return MealEventTable.MEAL_ID + " = " + meal.getConcretePrimaryKey().getId();
            }
        });

        assertThat(mealEvents.length, is(1));

        EventTable.Row[] events = HealthDatabase.EVENT_TABLE.select(db, new Where() {
            @Override
            public String getWhere() {
                return EventTable._ID + " = " + mealEvents[0].getConcretePrimaryKey().getEventId().getId();
            }
        });

        assertThat(events.length, is(1));
        assertThat(events[0].getTypeId().getId(), is(EventTypeTable.MEAL.getId()));
    }
}
