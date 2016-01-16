package test;

import android.app.Activity;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import javax.annotation.Nonnull;

import static org.mockito.Mockito.doReturn;

public class ActivityTestContext<T extends Activity> {

    @Nonnull
    private ActivityController<T> mActivityController;

    @Nonnull
    private T mActivity;

    @Nonnull
    private DateTime mNow;

    public ActivityTestContext(@Nonnull Object testCase, @Nonnull Class<T> activityClass) {
        MockitoAnnotations.initMocks(testCase);

        Utils.Db.TestData.cleanOldData();

        mNow = DateTimeConverter.now();

        mActivityController = Robolectric.buildActivity(activityClass);

        mActivity = mActivityController.get();
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

    public void close() {
        Utils.Db.TestData.cleanOldData();
        HealthDbHelper.closeDb();
    }

    public void pressOk() {
        MenuItem item = Mockito.mock(MenuItem.class);
        doReturn(R.id.action_modify).when(item).getItemId();
        getActivity().onOptionsItemSelected(item);
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
    }
}
