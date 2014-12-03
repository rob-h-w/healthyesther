package com.robwilliamson.healthyesther.dialog;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.healthyesther.add.ScoreActivity;
import com.robwilliamson.healthyesther.test.EditScoreDialogAccessor;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

public class EditScoreDialogTest extends ActivityInstrumentationTestCase2<ScoreActivity> {
    public EditScoreDialogTest() {
        super(ScoreActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        getActivity();
    }

    public void testOpenExisting() {
        HealthScoreActivityAccessor.editScore("Happiness", "Sad", "Happy");
        final HealthScore.Score score = new HealthScore.Score("Happiness", 5, true, "Sad", "Happy");
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return EditScoreDialogTest.this;
            }

            @Override
            public void checkContent() {
                EditScoreDialogAccessor.checkUnmodifiedContent(score);
            }
        });
    }
}
