package com.robwilliamson.healthyesther;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.db.*;
import com.robwilliamson.db.Utils;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MenuAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import junit.framework.Assert;

import java.io.File;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isClickable;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.robwilliamson.healthyesther.test.HomeActivityAccessor.healthScoreButton;
import static org.hamcrest.Matchers.not;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final String DROPBOX_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/Android/data/com.dropbox.android";
    private static final String DB_PATH = DROPBOX_PATH + "/files/scratch";
    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.showNavigationDrawer(false, getInstrumentation().getTargetContext());

        com.robwilliamson.db.Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(
                getInstrumentation().getTargetContext()).getWritableDatabase());

        if (Utils.File.exists(DROPBOX_PATH)) {
            File file = new File(DROPBOX_PATH);
            file.delete();
        }

        getActivity();
    }

    public void testActivityContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.checkUnmodifiedContent();
            }
        });
    }

    public void testMenuContents() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.checkMenuContent();
            }
        });
    }

    public void testBackupToDropboxDisabled() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.backupToDropbox()).check(matches(isDisplayed()));
        onView(MenuAccessor.backupToDropbox()).check(matches(not(isClickable())));
    }

    public void testBackupToDropbox() {
        Utils.File.mkdirs(DB_PATH);
        Assert.assertFalse(Utils.File.exists(Utils.File.Dropbox.dbFile()));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.backupToDropbox()).perform(click());
        onView(healthScoreButton()).check(matches(isDisplayed()));
        Assert.assertTrue(Utils.File.exists(Utils.File.Dropbox.dbFile()));
    }
}
