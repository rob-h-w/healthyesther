/*
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.edit;

import android.content.Intent;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import net.danlew.android.joda.JodaTimeAndroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
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

@RunWith(RobolectricTestRunner.class)
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
        JodaTimeAndroid.init(RuntimeEnvironment.application);
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

    @Test
    public void whenNewScoreEventIsAdded_createsANewScoreEventWithScore() {
        newScoreEventIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        HealthScoreEventTable.Row scoreEvent = HealthDatabase.HEALTH_SCORE_EVENT_TABLE.select1(db, WhereContains.foreignKey(HealthScoreEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));

        assertThat(scoreEvent.getScore(), is(4L));
    }

    @Test
    public void whenNewScoreEventIsAdded_doesNotCreateAScore() {
        newScoreEventIsAdded();

        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row[] scores = HealthDatabase.HEALTH_SCORE_TABLE.select(db, WhereContains.any());

        assertThat(scores.length, is(DEFAULT_SCORE_TITLES.length));
    }

    @Test
    public void whenLaunchedWithExistingScore_showsEventName() {
        launchedWithExistingScore();

        assertThat(mEventFragmentAccessor.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenLaunchedWithExistingScore_showsHappinessScoreValue() {
        launchedWithExistingScore();

        //noinspection ConstantConditions
        assertThat(mScoreEventGroupFramgentAccessor.getScore(HAPPINESS).getRating(), is(4f));
    }

    @Test
    public void whenLaunchedWithExistingScore_doesNotShowUnsetScoreValues() {
        launchedWithExistingScore();

        assertThat(mScoreEventGroupFramgentAccessor.getScore(ENERGY), is((EditScoreEventAccessor) null));
        assertThat(mScoreEventGroupFramgentAccessor.getScore(DROWSINESS), is((EditScoreEventAccessor) null));
    }

    @Test
    public void whenExistingScoreIsEdited_updatesEventName() {
        existingScoreIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row event = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());

        assertThat(event.getName(), is(EDITED_EVENT_NAME));
    }

    @Test
    public void whenExistingScoreIsEdited_doesNotCreateNewScoreEvent() {
        existingScoreIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row event = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        HealthDatabase.HEALTH_SCORE_EVENT_TABLE.select1(db, WhereContains.foreignKey(HealthScoreEventTable.EVENT_ID, event.getConcretePrimaryKey().getId()));
    }

    @Test
    public void whenExistingScoreIsEdited_updatesTheScore() {
        existingScoreIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row event = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        HealthScoreEventTable.Row scoreEvent = HealthDatabase.HEALTH_SCORE_EVENT_TABLE.select1(db, WhereContains.foreignKey(HealthScoreEventTable.EVENT_ID, event.getConcretePrimaryKey().getId()));

        //noinspection ConstantConditions
        assertThat(scoreEvent.getScore().intValue(), is(5));
    }

    private void existingScoreIsEdited() {
        launchedWithExistingScore();

        mEventFragmentAccessor.setName(EDITED_EVENT_NAME);

        //noinspection ConstantConditions
        mScoreEventGroupFramgentAccessor.getScore(HAPPINESS).setRating(5f);

        mContext.pressOk();

        Robolectric.flushForegroundThreadScheduler();
        Robolectric.flushBackgroundThreadScheduler();
    }

    private void launchedWithExistingScore() {
        Database db = HealthDbHelper.getDatabase();
        HealthScoreTable.Row[] scores = HealthDatabase.HEALTH_SCORE_TABLE.select(db, WhereContains.any());

        EventTable.Row event;
        try (Transaction transaction = db.getTransaction()) {
            event = new EventTable.Row(
                    EventTypeTable.HEALTH.getId(),
                    mContext.getNow(),
                    mContext.getNow(),
                    null,
                    EVENT_NAME
            );

            event.applyTo(transaction);

            for (HealthScoreTable.Row scoreType : scores) {
                if (scoreType.getName().equals(HAPPINESS)) {
                    HealthScoreEventTable.Row scoreEvent = new HealthScoreEventTable.Row(event.getNextPrimaryKey(), scoreType.getNextPrimaryKey(), 4L);
                    scoreEvent.applyTo(transaction);
                    break;
                }
            }

            transaction.commit();
            Robolectric.flushBackgroundThreadScheduler();
        }

        Intent intent = new Intent();
        intent.putExtra(HealthDatabase.EVENT_TABLE.getName(), event);
        mContext.reset(intent);
        mContext.getActivityController().setup();
    }

    private void newScoreEventIsAdded() {
        coreEventEditorIsShown();

        mEventFragmentAccessor.setName(EVENT_NAME);
        //noinspection ConstantConditions
        mScoreEventGroupFramgentAccessor.getScore(HAPPINESS).setRating(4);

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
