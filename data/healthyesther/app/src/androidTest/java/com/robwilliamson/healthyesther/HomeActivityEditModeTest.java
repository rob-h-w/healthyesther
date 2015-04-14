package com.robwilliamson.healthyesther;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

public class HomeActivityEditModeTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public HomeActivityEditModeTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        com.robwilliamson.healthyesther.db.Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(
                getInstrumentation().getTargetContext()).getWritableDatabase());

        getActivity();

        HomeActivityAccessor.EditMode.start();
    }

    public void testEditModeContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityEditModeTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.EditMode.checkUnmodifiedContent();
            }
        });
    }
}
