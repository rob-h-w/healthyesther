package com.robwilliamson.healthyesther;

import android.support.annotation.NonNull;

import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.use.QueryUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DbActivityTest {
    private ActivityController<TestableDbActivity> mActivityController;
    private TestableDbActivity mActivity;

    @Before
    public void setup() {
        mActivityController = Robolectric.buildActivity(TestableDbActivity.class);
        mActivity = mActivityController.get();
    }

    @Test
    public void beforeResume_executorIsNull() {
        mActivityController.create().start();

        assertThat(mActivity.getExecutor(), nullValue());
    }

    @Test
    public void onResume_createsExecutor() {
        mActivityController.create().start().resume();

        assertThat(mActivity.getExecutor(), notNullValue());
    }

    private static class TestableDbActivity extends DbActivity {
        public TransactionExecutor executor;

        @Override
        protected TransactionExecutor getExecutor() {
            if (executor == null) {
                return super.getExecutor();
            }

            return executor;
        }

        /**
         * An array of query users.
         *
         * @return The query users, or an empty array if no query is required.
         */
        @Override
        public QueryUser[] getQueryUsers() {
            return new QueryUser[0];
        }
    }
}
