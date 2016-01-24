package com.robwilliamson.healthyesther.edit;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import test.ActivityTestContext;
import test.view.EditEventFragmentAccessor;
import test.view.EditScoreEventAccessor;
import test.view.EditScoreEventGroupAccessor;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ScoreEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String HAPPINESS = "Happiness";
    private static final String ENERGY = "Energy";
    private static final String DROWSINESS = "Drowsiness";
    private static final String EDITED_EVENT_NAME = "My Score!";

    private static final String[] DEFAULT_SCORE_TITLES = {
            HAPPINESS,
            ENERGY,
            DROWSINESS
    };

    private ActivityTestContext<TestableScoreEventActivity> mContext;

    private EditEventFragmentAccessor mEventFragmentAccessor;
    private EditScoreEventGroupAccessor mScoreEventGroupFramgentAccessor;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableScoreEventActivity.class);

        mEventFragmentAccessor = new EditEventFragmentAccessor(mContext);
        mScoreEventGroupFramgentAccessor = new EditScoreEventGroupAccessor(mContext);
    }

    @After
    public void teardown() {
        mContext.close();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void whenScoreEventEditorIsShown_containsDefaultScores() {
        coreEventEditorIsShown();

        final int count = mScoreEventGroupFramgentAccessor.getScoreCount();
        assertThat(count, is(DEFAULT_SCORE_TITLES.length));

        for (int i = 0; i < count; i++) {
            EditScoreEventAccessor scoreEventAccessor = mScoreEventGroupFramgentAccessor.getScore(i);
            ScoreType type = ScoreType.fromName(scoreEventAccessor.getName());

            assertThat(scoreEventAccessor.getMaximumLabel(), is(type.max));
            assertThat(scoreEventAccessor.getMinimumLabel(), is(type.min));
        }
    }

    @Test
    public void whenScoreEventEditorIsShown_containsDefaultScoreTitles() {
        coreEventEditorIsShown();

        for (int i = 0; i < DEFAULT_SCORE_TITLES.length; i++) {
            //noinspection ConstantConditions
            assertThat(mScoreEventGroupFramgentAccessor.getScore(i).getName(), isIn(DEFAULT_SCORE_TITLES));
        }
    }

    @Test
    public void whenNewScoreEventIsAdded_createsANewEventWithNameSet() {
        newScoreEventIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());

        assertThat(row.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenNewScoreEventIsAdded_createsANewScoreEvent() {
        newScoreEventIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        HealthScoreEventTable.Row[] scoreEvent = HealthDatabase.HEALTH_SCORE_EVENT_TABLE.select(db, WhereContains.foreignKey(HealthScoreEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));

        assertThat(scoreEvent.length, is(1));
    }

    private void newScoreEventIsAdded() {
        coreEventEditorIsShown();

        mEventFragmentAccessor.setName(EVENT_NAME);
        //noinspection ConstantConditions
        mScoreEventGroupFramgentAccessor.getScore(HAPPINESS).setRating(5);

        mContext.pressOk();

        Robolectric.flushForegroundThreadScheduler();
        Robolectric.flushBackgroundThreadScheduler();
    }

    private void coreEventEditorIsShown() {
        mContext.getActivityController().setup();
    }

    private enum ScoreType {
        Happiness("Happiness", "Sad", "Happy", 5),
        Energy("Energy", "Tired", "Energetic", 5),
        Drowsiness("Drowsiness", "Awake", "Sleepy", 1),
        Foonsness("Foonsness", "Blank", "Foons Balloons", 3);

        @Nonnull
        public final String scoreTypeName;
        @Nullable
        public final String min;
        @Nullable
        public final String max;
        public final float best;

        ScoreType(@Nonnull String name, @Nullable String min, @Nullable String max, float best) {
            this.scoreTypeName = name;
            this.max = max;
            this.min = min;
            this.best = best;
        }

        @Nullable
        public static ScoreType fromName(@Nonnull String name) {
            for (ScoreType type : values()) {
                if (type.scoreTypeName.equals(name)) {
                    return type;
                }
            }

            return null;
        }
    }

    private static class TestableScoreEventActivity extends ScoreEventActivity {
        private boolean mBusy = false;

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
