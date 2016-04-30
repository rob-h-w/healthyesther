package com.robwilliamson.healthyesther.add;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.Utils;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

public class ScoreActivityTest extends ActivityInstrumentationTestCase2<ScoreActivity> {
    public ScoreActivityTest() {
        super(ScoreActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());

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
