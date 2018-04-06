/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.edit;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.EditAccessor;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;
import com.robwilliamson.healthyesther.test.ScoreActivityAccessor;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class HealthScoreActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    @Before
    public void setUp() throws Exception {
        HomeActivityAccessor.setShowNavigationDrawer(false);

        Utils.Db.TestData.cleanOldData();
        Settings.INSTANCE.resetExclusionList();

        HomeActivityAccessor.AddMode.start();

        onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
    }

    @Test
    public void testOpenAddScoreActivity() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                HealthScoreActivityAccessor.checkUnmodifiedContent();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void testHideScoreActivity() {
        HealthScoreActivityAccessor.hideScore("Happiness");
        onView(HealthScoreActivityAccessor.editScoreGroupLayout()).check(matches(
                not(withChild(HealthScoreActivityAccessor.score("Happiness", "Sad", "Happy")))));
        Assert.assertTrue(Settings.INSTANCE.getDefaultExcludedEditScores().contains("Happiness"));
    }

    @Test
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

    @Test
    public void test_addScore_updatesDatabase() {
        onView(HealthScoreActivityAccessor.score("Happiness", "Sad", "Happy")).perform(click());

        onView(EditAccessor.ok()).perform(click());
    }

    @Test
    public void test_emptyName_cannotCommit() {
        onView(EditAccessor.ok()).check(doesNotExist());
    }

    @Test
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

    @Test
    public void test_createNewScoreType_showsScoreInHealthScoreActivity() {
        final String SLOON = "Sloon";
        onView(HealthScoreActivityAccessor.trackAnotherScoreButton())
                .perform(scrollTo())
                .perform(click());

        onView(ScoreActivityAccessor.scoreName()).perform(click());
        onView(ScoreActivityAccessor.scoreName()).perform(typeText(SLOON));
        onView(EditAccessor.ok()).perform(click());

        onView(HealthScoreActivityAccessor.editScoreGroupLayout()).check(matches(hasDescendant(withText(SLOON))));
    }
}
