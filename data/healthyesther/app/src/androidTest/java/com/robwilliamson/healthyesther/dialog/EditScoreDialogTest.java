package com.robwilliamson.healthyesther.dialog;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.add.ScoreActivity;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
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

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());
        Settings.INSTANCE.resetExclusionList();

        getActivity();
    }

    public void testOpenExisting() {
        HealthScoreActivityAccessor.editScore("Happiness");
        final HealthScore.Value score = new HealthScore.Value("Happiness", 5, true, "Sad", "Happy");
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
