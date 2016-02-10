package com.robwilliamson.healthyesther.edit;

import com.robwilliamson.healthyesther.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import test.ActivityTestContext;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ScoreActivityTest {
    private static final String SCORE_NAME = "Foonsness";
    private ActivityTestContext<TestableScoreActivity> mContext;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableScoreActivity.class);
    }

    @Test
    public void whenANewScoreIsCreated_scoreIsInDatabase() {
        //
    }

    private void aNewScoreIsCreated() {
        mContext.getActivityController().setup();
    }

    private static class TestableScoreActivity extends ScoreActivity {
        private volatile boolean mBusy;

        @Override
        protected boolean isBusy() {
            return mBusy;
        }

        @Override
        protected void setBusy(boolean busy) {
            mBusy = busy;
        }
    }
}
