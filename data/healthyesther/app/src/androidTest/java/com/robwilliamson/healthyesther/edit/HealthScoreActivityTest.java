package com.robwilliamson.healthyesther.edit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.view.View;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.EditAccessor;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import junit.framework.Assert;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class HealthScoreActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    public HealthScoreActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData();
        Settings.INSTANCE.resetExclusionList();

        getActivity();

        HomeActivityAccessor.AddMode.start();

        onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
    }

    public void testOpenAddScoreActivity() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HealthScoreActivityTest.this;
            }

            @Override
            public void checkContent() {
                HealthScoreActivityAccessor.checkUnmodifiedContent();
            }
        });
    }

    public void testHideScoreActivity() {
        HealthScoreActivityAccessor.hideScore("Happiness");
        onView(HealthScoreActivityAccessor.editScoreGroupLayout()).check(matches(
                not(withChild(HealthScoreActivityAccessor.score("Happiness", "Sad", "Happy")))));
        Assert.assertTrue(Settings.INSTANCE.getDefaultExcludedEditScores().contains("Happiness"));
    }

    public void testHideMultipleScores() {
        HealthScoreActivityAccessor.hideScore("Happiness");
        HealthScoreActivityAccessor.hideScore("Energy");
        onView(HealthScoreActivityAccessor.editScoreGroupLayout()).check(matches(
                not(withChild(HealthScoreActivityAccessor.score("Happiness", "Sad", "Happy")))));
        onView(HealthScoreActivityAccessor.editScoreGroupLayout()).check(matches(
                not(withChild(HealthScoreActivityAccessor.score("Energy", "Tired", "Energetic")))));
        Assert.assertTrue(Settings.INSTANCE.getDefaultExcludedEditScores().contains("Happiness"));
        Assert.assertTrue(Settings.INSTANCE.getDefaultExcludedEditScores().contains("Energy"));
    }

    public void test_addScore_updatesDatabase() {
        onView(HealthScoreActivityAccessor.score("Happiness", "Sad", "Happy")).perform(click());

        onView(EditAccessor.ok()).perform(click());
    }

    public void test_emptyName_cannotCommit() {
        onView(EditAccessor.ok()).check(doesNotExist());
    }

    public void test_setARatingThenEdit_doesNotCrash() {
        Matcher<View> happinessScore = HealthScoreActivityAccessor.score("Happiness", "Sad", "Happy");
        onView(happinessScore).perform(click());
        onView(HealthScoreActivityAccessor.scoreTitle("Happiness")).perform(longClick());
        onView(withText("Edit")).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Happiness")).check(matches(isDisplayed()));
        onView(withText("Sad")).check(matches(isDisplayed()));
        onView(withText("Happy")).check(matches(isDisplayed()));
    }
}
