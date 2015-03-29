package com.robwilliamson.healthyesther.dialog;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;
import com.robwilliamson.healthyesther.test.TrackAnotherScoreDialogAccessor;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;

public class TrackAnotherScoreDialogTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    public TrackAnotherScoreDialogTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());

        getActivity();
    }

    public void testAddOne() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return TrackAnotherScoreDialogTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.AddMode.start();
                onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
                addScoreType("Score 1");
                pressBack();
                onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
                checkScoreIsPresent("Score 1");
                pressBack();
                Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());
            }
        });
    }

    public void testAddMore() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return TrackAnotherScoreDialogTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.AddMode.start();
                onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
                addScoreType("A score");
                addScoreType("Another score");
                addScoreType("Yet another score");
                pressBack();
                onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
                checkScoreIsPresent("A score");
                checkScoreIsPresent("Another score");
                checkScoreIsPresent("Yet another score");
                pressBack();
                Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());
            }
        });
    }

    private void addScoreType(String name) {
        onView(HealthScoreActivityAccessor.trackAnotherScoreButton()).perform(click());
        onView(TrackAnotherScoreDialogAccessor.healthScoreEditBox()).perform(typeText(name));
        closeSoftKeyboard();
        sleep();
        onView(TrackAnotherScoreDialogAccessor.okButton()).check(matches(isDisplayed()));
        onView(TrackAnotherScoreDialogAccessor.okButton()).perform(click());
    }

    private void checkScoreIsPresent(String name) {
        onView(HealthScoreActivityAccessor.scoreTitle(name)).check(matches(isEnabled()));
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
