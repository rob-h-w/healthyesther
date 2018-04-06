/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

public class ActivityTestContext<T extends Activity> {

    @Nonnull
    private final DateTime mNow;
    @Nonnull
    private final Class<T> mActivityClass;
    @SuppressWarnings("NullableProblems")
    @Nonnull
    private ActivityController<T> mActivityController;
    @SuppressWarnings("NullableProblems")
    @Nonnull
    private T mActivity;

    public ActivityTestContext(@Nonnull Object testCase, @Nonnull Class<T> activityClass) {
        MockitoAnnotations.initMocks(testCase);

        Utils.Db.TestData.cleanOldData();

        mNow = DateTimeConverter.now();

        mActivityClass = activityClass;

        reset();
    }

    @Nonnull
    public ActivityController<T> getActivityController() {
        return mActivityController;
    }

    @Nonnull
    public T getActivity() {
        return mActivity;
    }

    @Nonnull
    public DateTime getNow() {
        return mNow;
    }

    public void reset() {
        this.reset(null);
    }

    public void reset(@Nullable Intent intent) {
        if (intent != null) {
            mActivityController = Robolectric.buildActivity(mActivityClass, intent);
        } else {
            mActivityController = Robolectric.buildActivity(mActivityClass);
        }

        mActivity = mActivityController.get();
    }

    public void close() {
        Utils.Db.TestData.cleanOldData();
        HealthDbHelper.closeDb();
    }

    public void pressOk() {
        Activity activity = getActivity();
        if (!activity.onPrepareOptionsMenu(Mockito.mock(Menu.class))) {
            fail("The OK method was not enabled.");
        }

        MenuItem item = Mockito.mock(MenuItem.class);
        doReturn(R.id.action_modify).when(item).getItemId();
        activity.onOptionsItemSelected(item);

        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
    }

    public void configurationChange() {
        Bundle bundle = new Bundle();
        mActivityController.saveInstanceState(bundle).pause().stop().destroy();
        mActivityController = Robolectric.buildActivity(mActivityClass).create(bundle).start().restoreInstanceState(bundle).resume();
        mActivity = mActivityController.get();
    }
}
