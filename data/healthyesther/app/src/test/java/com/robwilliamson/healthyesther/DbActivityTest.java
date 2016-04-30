package com.robwilliamson.healthyesther;

import android.support.annotation.NonNull;

import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.hamcrest.CoreMatchers.notNullValue;
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

    @Test(expected = NullPointerException.class)
    public void beforeAttach_executorIsNull() {
        mActivity.getExecutor();
    }

    @Test
    public void onResume_createsExecutor() {
        mActivityController.create().start().resume();

        assertThat(mActivity.getExecutor(), notNullValue());
    }

    private static class TestableDbActivity extends DbActivity {
        public TransactionExecutor executor;

        @NonNull
        @Override
        public TransactionExecutor getExecutor() {
            if (executor == null) {
                return super.getExecutor();
            }

            return executor;
        }
    }
}
