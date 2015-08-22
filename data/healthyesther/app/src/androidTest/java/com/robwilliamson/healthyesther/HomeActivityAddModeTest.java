package com.robwilliamson.healthyesther;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

public class HomeActivityAddModeTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final String DROPBOX_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/Android/data/com.dropbox.android";
    private static final String DB_PATH = DROPBOX_PATH + "/files/scratch";

    public HomeActivityAddModeTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(
                getInstrumentation().getTargetContext()).getWritableDatabase());

        getActivity();

        HomeActivityAccessor.AddMode.start();
    }

    public void testAddModeContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityAddModeTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.AddMode.checkUnmodifiedContent();
            }
        });
    }
}
