package com.robwilliamson.healthyesther.fragment.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.annotation.Nullable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditFragmentTest {
    private TestableEditFragment mEditFragment;

    @Mock
    private Context mContext;

    @Mock
    private EditFragment.WatcherCaller<Object> mWatcherCaller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mEditFragment = new TestableEditFragment(Object.class);
    }

    @Test
    public void onAttach_assignsWatcher() {
        mEditFragment.onAttach(mContext);

        assertThat(mEditFragment.getWatcher(), is((Object) mContext));
    }

    @Test
    public void onDetach_nullsWatcher() {
        // Ensure that the watcher is first not null.
        mEditFragment.onAttach(mContext);

        mEditFragment.onDetach();

        assertThat(mEditFragment.getWatcher(), is((Object) null));
    }

    @Test
    public void callWatcherWithNullWatcher_doesNothing() {
        mEditFragment.callWatcher(mWatcherCaller);

        verifyZeroInteractions(mWatcherCaller);
    }

    @Test
    public void callWatcherWithWatcher_callsTheWatcherCaller() {
        mEditFragment.onAttach(mContext);

        mEditFragment.callWatcher(mWatcherCaller);

        verify(mWatcherCaller).call(mContext);
    }

    @SuppressLint("ValidFragment")
    private static class TestableEditFragment extends EditFragment<Object> {
        private Object mWatcher;

        public TestableEditFragment(Class<Object> type) {
            super(type);
        }

        public Object getWatcher() {
            mWatcher = null;
            updateWatcher();
            return mWatcher;
        }

        @Nullable
        @Override
        public Modification getModification() {
            return null;
        }

        @Override
        public boolean validate() {
            return false;
        }

        @Override
        protected void updateWatcher(@NonNull Object watcher) {
            mWatcher = watcher;
        }

        @Override
        protected int getFragmentLayout() {
            return 0;
        }

        @Override
        public Query[] getQueries() {
            return new Query[0];
        }

        @Override
        public void callWatcher(@NonNull WatcherCaller<Object> call) {
            super.callWatcher(call);
        }
    }
}
