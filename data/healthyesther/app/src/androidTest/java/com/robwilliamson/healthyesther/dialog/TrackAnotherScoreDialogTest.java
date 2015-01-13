package com.robwilliamson.healthyesther.dialog;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.add.ScoreActivity;
import com.robwilliamson.healthyesther.test.EditScoreDialogAccessor;
import com.robwilliamson.healthyesther.test.Espresso;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;
import com.robwilliamson.healthyesther.test.TrackAnotherScoreDialogAccessor;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;

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
                // Open the health score activity.
                onView(HomeActivityAccessor.healthScoreButton()).perform(click());

                // Name a new score to track.
                onView(HealthScoreActivityAccessor.trackAnotherScoreButton()).perform(click());
                onView(TrackAnotherScoreDialogAccessor.healthScoreEditBox()).perform(typeText("Score 1"));

                // Close the dialog
                onView(TrackAnotherScoreDialogAccessor.okButton()).perform(click());

                // Close the score tracker
                com.google.android.apps.common.testing.ui.espresso.Espresso.pressBack();

                // Check that the new content is added.
                onView(HomeActivityAccessor.healthScoreButton()).perform(click());
                onView(HealthScoreActivityAccessor.scoreTitle("Score 1")).check(matches(isDisplayed()));
            }
        });
    }
}
