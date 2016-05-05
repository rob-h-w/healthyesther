/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BaseFragmentActivityTest {
    @Nonnull
    ActivityController<TestableBaseFragmentActivity> mActivityController = Robolectric.buildActivity(TestableBaseFragmentActivity.class);
    @Nonnull
    private TestableBaseFragmentActivity mFragmentActivity = mActivityController.get();

    @Mock
    private Window.Callback mCallback;

    @Mock
    private ViewGroup mViewRoot;

    @Mock
    private View mDecorView;

    @Mock
    private Toolbar mToolbar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        /*
        Window window = mFragmentActivity.getWindow();
        doReturn(mCallback).when(window).getCallback();
        doReturn(mViewRoot).when(window).findViewById(android.R.id.content);
        doReturn(mToolbar).when(window).findViewById(R.id.toolbar);
        doReturn(mDecorView).when(window).getDecorView();*/
    }

    @Test
    public void onCreate_setsBackgroundDrawable() {
        mActivityController.create();

        verify(mFragmentActivity.getWindow()).setBackgroundDrawableResource(R.drawable.background);
    }

    @Test
    public void onCreate_setsContentView() {
        mActivityController.create();

        assertThat(mFragmentActivity.getContentView(), is(R.layout.activity_base_root));
    }

    @Test
    public void onCreateOptionsMenu_inflatesGlobalMenu() {
        mActivityController.create().start().resume();//.visible(); // Ensure the activity is all the way shown
        Menu menu = mock(Menu.class);
        mFragmentActivity.onCreateOptionsMenu(menu);

        verify(mFragmentActivity.getMenuInflater()).inflate(R.menu.global, menu);
    }

    @Test
    public void onOptionsItemSelectedWithActionSettings_handlesSettings() {
        mActivityController.create().start().resume();//.visible(); // Ensure the activity is all the way shown
        MenuItem item = mock(MenuItem.class);
        doReturn(R.id.action_settings).when(item).getItemId();

        assertThat(mFragmentActivity.onOptionsItemSelected(item), is(true));

        assertNotNull(mFragmentActivity.getIntent());

        assertThat(mFragmentActivity.getIntent().getComponent().getClassName(),
                is(SettingsActivity.class.getName()));
    }

    @Test
    public void onOptionsItemSelectedWithOtherAction_ignores() {
        mActivityController.create().start().resume();
        MenuItem item = mock(MenuItem.class);
        doReturn(R.id.action_backup_to_dropbox).when(item).getItemId();

        assertThat(mFragmentActivity.onOptionsItemSelected(item), is(false));
    }

    @Test
    public void onPause_setsActiveFalse() {
        mActivityController.setup().pause();

        assertThat(mFragmentActivity.isActive(), is(false));
    }

    @Test
    public void onResume_setsActiveTrue() {
        mActivityController.setup();

        assertThat(mFragmentActivity.isActive(), is(true));
    }

    @Test
    public void getContentLayoutResourceId_returnsCorrectId() {
        assertThat(mFragmentActivity.getContentLayoutResourceId(), is(R.layout.activity_base_root));
    }

    @Test
    public void getActivityContentLayoutReosurceId_callsFindViewById() {
        mActivityController.setup();
        View layout = Utils.checkNotNull(mFragmentActivity.getActivityContentLayout());

        assertThat(layout.getId(), is(mFragmentActivity.getActivityContentLayoutResourceId()));
    }

    private static class TestableBaseFragmentActivity extends BaseFragmentActivity {

        @Nullable
        private Integer mContentLayoutResourceId;

        @Nonnull
        private MenuInflater mMenuInflater = mock(MenuInflater.class);

        @Nullable
        private Intent mIntent;

        @Nullable
        private Window mWindow;

        public Integer getContentView() {
            return mContentLayoutResourceId;
        }

        @Override
        public void setContentView(int layoutResID) {
            mContentLayoutResourceId = layoutResID;
            super.setContentView(layoutResID);
        }

        @Override
        public boolean isActive() {
            return super.isActive();
        }

        @NonNull
        @Override
        public MenuInflater getMenuInflater() {
            return mMenuInflater;
        }

        @Override
        public void startActivity(Intent intent) {
            mIntent = intent;
            super.startActivity(intent);
        }

        @Nullable
        public Intent getIntent() {
            return mIntent;
        }

        @Override
        public Window getWindow() {
            if (mWindow == null) {
                mWindow = Mockito.spy(super.getWindow());
            }

            return mWindow;
        }
    }
}
