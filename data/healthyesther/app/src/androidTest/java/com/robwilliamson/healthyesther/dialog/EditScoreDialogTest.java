/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.dialog;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.edit.ScoreEventActivity;
import com.robwilliamson.healthyesther.test.EditScoreFragmentAccessor;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

@RunWith(AndroidJUnit4.class)
public class EditScoreDialogTest {
    @Rule
    public ActivityTestRule<ScoreEventActivity> mActivityRule = new ActivityTestRule<>(
            ScoreEventActivity.class);


    @Before
    public void setUp() throws Exception {
        Utils.Db.TestData.cleanOldData();
        Settings.INSTANCE.resetExclusionList();
    }

    @Test
    public void testOpenExisting() {
        HealthScoreActivityAccessor.editScore("Happiness");
        final HealthScoreTable.Row score = new HealthScoreTable.Row("Happiness", true, "Happy", "Sad");
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                EditScoreFragmentAccessor.checkUnmodifiedContent(score);
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }
}
