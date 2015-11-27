package com.robwilliamson.healthyesther.db.integration;

import android.content.Context;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.annotation.Nonnull;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ExecutorTest {
    @Mock
    private Context mContext;

    @Mock
    private Database mDatabase;

    @Mock
    private TransactionExecutor.Observer mObserver;

    @Mock
    private Runnable mRunnable;

    private TestableExecutor mExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Robolectric.getBackgroundThreadScheduler().reset();
        doReturn(mContext).when(mContext).getApplicationContext();
        mExecutor = new TestableExecutor(mObserver);
    }

    @Test(expected = IllegalStateException.class)
    public void runAsynchronouslyCalledTwice_throws() {
        Robolectric.getBackgroundThreadScheduler().pause();
        mExecutor.runAsynchronously(mRunnable);
        mExecutor.runAsynchronously(mRunnable);
    }

    @Test
    public void runAsynchronouslyCalledTwiceAfterExecuting_callsRunTwice() {
        mExecutor.runAsynchronously(mRunnable);
        mExecutor.runAsynchronously(mRunnable);

        verify(mRunnable, times(2)).run();
    }

    @Test
    public void cancelCalledOnIdle_doesNothing() {
        mExecutor.cancel();

        verifyZeroInteractions(mDatabase, mObserver, mRunnable);
    }

    @Test
    public void cancelWhenPending_doesNotExecuteRun() {
        Robolectric.getBackgroundThreadScheduler().pause();
        mExecutor.runAsynchronously(mRunnable);

        mExecutor.cancel();

        Robolectric.getBackgroundThreadScheduler().unPause();

        verifyZeroInteractions(mRunnable);
    }

    private static class TestableExecutor extends Executor {
        public TestableExecutor(@Nonnull Observer observer) {
            super(observer);
        }

        @Override
        public synchronized void runAsynchronously(@Nonnull Runnable runnable) {
            super.runAsynchronously(runnable);
        }
    }
}
