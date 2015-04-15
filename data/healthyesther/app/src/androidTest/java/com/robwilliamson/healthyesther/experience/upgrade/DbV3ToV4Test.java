package com.robwilliamson.healthyesther.experience.upgrade;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.test.Database;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

public class DbV3ToV4Test extends ActivityInstrumentationTestCase2<HomeActivity> {
    public DbV3ToV4Test() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Database.useV3Database(getInstrumentation());

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        Database.deleteDatabase(getInstrumentation().getTargetContext());

        super.tearDown();
    }

    public void testMenuContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return DbV3ToV4Test.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.checkUnmodifiedMenuContent(getInstrumentation().getTargetContext());
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
