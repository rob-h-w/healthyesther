package com.robwilliamson.healthyesther.dialog;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.EditAccessor;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;
import com.robwilliamson.healthyesther.test.TrackAnotherScoreDialogAccessor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;

@RunWith(AndroidJUnit4.class)
public class TrackAnotherScoreDialogTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);


    @Before
    public void setUp() throws Exception {
        Utils.Db.TestData.cleanOldData();
    }

    @Test
    public void testAddOne() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                HomeActivityAccessor.AddMode.start();
                onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
                addScoreType("Score 1");
                pressBack();
                onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
                checkScoreIsPresent("Score 1");
                pressBack();
                Utils.Db.TestData.cleanOldData();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void testAddMore() {
        Orientation.check(new Orientation.Subject() {
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
                Utils.Db.TestData.cleanOldData();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    private void addScoreType(String name) {
        onView(HealthScoreActivityAccessor.trackAnotherScoreButton()).perform(scrollTo()).perform(click());
        onView(TrackAnotherScoreDialogAccessor.healthScoreEditBox()).perform(typeText(name));
        closeSoftKeyboard();
        sleep();
        onView(EditAccessor.ok()).perform(click());
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
