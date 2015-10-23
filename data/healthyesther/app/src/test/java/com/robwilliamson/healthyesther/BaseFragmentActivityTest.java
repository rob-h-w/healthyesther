package com.robwilliamson.healthyesther;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
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
        mActivityController.create().start().resume().pause();

        assertThat(mFragmentActivity.isActive(), is(false));
    }

    @Test
    public void onResume_setsActiveTrue() {
        mActivityController.create().start().resume();

        assertThat(mFragmentActivity.isActive(), is(true));
    }

    @Test
    public void getContentLayoutResourceId_returnsCorrectId() {
        assertThat(mFragmentActivity.getContentLayoutResourceId(), is(R.layout.activity_base_root));
    }

    @Test
    public void getActivityContentLayoutReosurceId_callsFindViewById() {
        mFragmentActivity.getActivityContentLayout();

        assertThat(mFragmentActivity.getViewId(), is(mFragmentActivity.getActivityContentLayoutResourceId()));
    }

    private static class TestableBaseFragmentActivity extends BaseFragmentActivity {
        @Nonnull
        private Window mWindow = mock(Window.class);

        @Nullable
        private Integer mContentLayoutResourceId;

        @Nonnull
        private MenuInflater mMenuInflater = mock(MenuInflater.class);

        @Nullable
        private Intent mIntent;

        @Nullable
        private Integer mViewId;

        @Override
        public Window getWindow() {
            return mWindow;
        }

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
        public int getContentLayoutResourceId() {
            return super.getContentLayoutResourceId();
        }

        @Override
        public LinearLayout getActivityContentLayout() {
            return super.getActivityContentLayout();
        }

        @Override
        public View findViewById(int id) {
            mViewId = id;
            return null;
        }

        @Nullable
        public Integer getViewId() {
            return mViewId;
        }
    }
}
