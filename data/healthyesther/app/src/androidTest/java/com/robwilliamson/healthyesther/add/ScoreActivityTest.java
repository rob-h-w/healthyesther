package com.robwilliamson.healthyesther.add;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.Utils;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import junit.framework.Assert;

import java.util.HashSet;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withChild;
import static org.hamcrest.Matchers.not;

public class ScoreActivityTest extends ActivityInstrumentationTestCase2<ScoreActivity> {
    public ScoreActivityTest() {
        super(ScoreActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());
        Settings.INSTANCE.setDefaultEditScoreExclusionList(new HashSet<String>());

        getActivity();
    }

    public void testOpenAddScoreActivity() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return ScoreActivityTest.this;
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
}
