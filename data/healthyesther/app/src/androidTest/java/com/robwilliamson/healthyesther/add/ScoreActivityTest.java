package com.robwilliamson.healthyesther.add;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

public class ScoreActivityTest extends ActivityInstrumentationTestCase2<ScoreActivity> {
    public ScoreActivityTest() {
        super(ScoreActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

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
}
