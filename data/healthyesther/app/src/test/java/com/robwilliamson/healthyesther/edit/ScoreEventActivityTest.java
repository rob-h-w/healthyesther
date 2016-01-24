package com.robwilliamson.healthyesther.edit;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import test.ActivityTestContext;
import test.view.EditEventFragmentAccessor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ScoreEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String EDITED_EVENT_NAME = "My Score!";

    private ActivityTestContext<TestableScoreEventActivity> mContext;

    private EditEventFragmentAccessor mEventFragmentAccessor;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableScoreEventActivity.class);

        mEventFragmentAccessor = new EditEventFragmentAccessor(mContext);
    }

    @After
    public void teardown() {
        mContext.close();
    }

    @Test
    public void whenNewScoreEventIsAdded_createsANewEventWithNameSet() {
        newScoreEventIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());

        assertThat(row.getName(), is(EVENT_NAME));
    }

    private void newScoreEventIsAdded() {
        mContext.getActivityController().setup();

        mEventFragmentAccessor.setName(EVENT_NAME);

        mContext.pressOk();

        Robolectric.flushForegroundThreadScheduler();
        Robolectric.flushBackgroundThreadScheduler();
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
