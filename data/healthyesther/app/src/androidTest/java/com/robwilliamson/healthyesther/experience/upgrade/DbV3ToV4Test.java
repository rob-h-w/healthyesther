package com.robwilliamson.healthyesther.experience.upgrade;

import android.content.Context;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;

public class DbV3ToV4Test extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final String DROPBOX_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/Android/data/com.dropbox.android";
    private static final String DB_PATH = DROPBOX_PATH + "/files/scratch";
    public DbV3ToV4Test() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        useV3Database();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        deleteDatabase();

        super.tearDown();
    }

    private void useV3Database() throws IOException, NoSuchFieldException, IllegalAccessException {
        String dbPath = getDatabaseAbsolutePath(); // Record the string before we delete the db
                                                   // because deletion also resets HealthDbHelper.

        deleteDatabase();

        Context testContext = getInstrumentation().getContext();

        int v3DbId = testContext.getResources().getIdentifier("v3", "raw", testContext.getPackageName());

        InputStream v3InputStream = testContext.getResources().openRawResource(v3DbId);

        try {
            Utils.File.copy(v3InputStream, dbPath);
        } finally {
            v3InputStream.close();
        }
    }

    private void deleteDatabase() throws NoSuchFieldException, IllegalAccessException {
        HealthDbHelper helper = HealthDbHelper.getInstance(getInstrumentation().getTargetContext());
        helper.close();

        File file = new File(getDatabaseAbsolutePath());

        if (file.exists()) {
            assertTrue("Unable to delete " + file.getAbsolutePath(), file.delete());
        }

        Field sInstance = HealthDbHelper.class.getDeclaredField("sInstance");
        sInstance.setAccessible(true);
        sInstance.set(null, null);
    }

    /**
     * Careful with this, it's a hack. It uses HealthDbHelper to get the path of the database.
     * HealthDbHelper's life must be managed carefully in order to set up the conditions of first
     * application startup with an old Db, and new code.
     */
    private String getDatabaseAbsolutePath() {
        Context targetContext = getInstrumentation().getTargetContext();
        return targetContext.getDatabasePath(
                HealthDbHelper.getInstance(targetContext).getDatabaseName()).getAbsolutePath();
    }

    public void testMenuContents() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return DbV3ToV4Test.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.checkMenuContent();
            }
        });
    }

    public void testAddScoreView() {
        HomeActivityAccessor.AddMode.start();
        onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return DbV3ToV4Test.this;
            }

            @Override
            public void checkContent() {
                HealthScoreActivityAccessor.checkUnmodifiedContent();
            }
        });
    }
}
