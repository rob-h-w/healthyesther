/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.edit;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import test.ActivityTestContext;
import test.view.EditScoreFragmentAccessor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ScoreActivityTest {
    private static final String SCORE_NAME = "Foonsness";
    private static final String MIN = "Not Foons";
    private static final String MAX = "All the Foons";
    private static final int BEST = 1;

    private ActivityTestContext<TestableScoreActivity> mContext;
    private EditScoreFragmentAccessor mScoreAccessor;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableScoreActivity.class);
        mScoreAccessor = new EditScoreFragmentAccessor(mContext);
    }

    @Test
    public void whenANewScoreIsCreated_scoreIsInDatabase() {
        Database db = HealthDbHelper.getDatabase();

        assertThat(HealthDatabase.HEALTH_SCORE_TABLE.select(db, WhereContains.any()).length, is(3));

        aNewScoreIsCreated();

        assertThat(HealthDatabase.HEALTH_SCORE_TABLE.select(db, WhereContains.any()).length, is(4));
    }

    @Test
    public void whenANewScoreIsCreated_scoreValuesAreSet() {
        aNewScoreIsCreated();

        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row score = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, SCORE_NAME));

        assertThat(score.getName(), is(SCORE_NAME));
        assertThat(score.getRandomQuery(), is(true));
        assertThat(score.getMaxLabel(), is(MAX));
        assertThat(score.getMinLabel(), is(MIN));
    }

    @Test
    public void whenANewScoreIsCreated_createsSingleNewHealthScoreJudgmentRange() {
        aNewScoreIsCreated();

        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row healthScoreRow = HealthDatabase.HEALTH_SCORE_TABLE
                .select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, SCORE_NAME));

        HealthScoreJudgmentRangeTable.Row judgmentRange = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE
                .select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, healthScoreRow.getConcretePrimaryKey().getId()));
    }

    @Test
    public void whenANewScoreIsCreated_createsSingleNewHealthScoreJudgmentRangeWithNullStartFinish() {
        aNewScoreIsCreated();

        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row healthScoreRow = HealthDatabase.HEALTH_SCORE_TABLE
                .select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, SCORE_NAME));

        HealthScoreJudgmentRangeTable.Row judgmentRange = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE
                .select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, healthScoreRow.getConcretePrimaryKey().getId()));

        assertThat(judgmentRange.getStartTime(), is((Long)null));
        assertThat(judgmentRange.getEndTime(), is((Long) null));
    }

    @Test
    public void whenANewScoreIsCreated_createsSingleNewHealthScoreJudgmentRangeWithPassedBestScore() {
        aNewScoreIsCreated();

        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row healthScoreRow = HealthDatabase.HEALTH_SCORE_TABLE
                .select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, SCORE_NAME));

        HealthScoreJudgmentRangeTable.Row judgmentRange = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE
                .select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, healthScoreRow.getConcretePrimaryKey().getId()));

        assertThat(judgmentRange.getBestValue(), is((long)BEST));
    }

    private void aNewScoreIsCreated() {
        mContext.getActivityController().setup();

        mScoreAccessor.setName(SCORE_NAME);
        mScoreAccessor.setAskPeriodically(true);
        mScoreAccessor.setMax(MAX);
        mScoreAccessor.setMin(MIN);
        mScoreAccessor.setBestScore(BEST);

        mContext.pressOk();
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
