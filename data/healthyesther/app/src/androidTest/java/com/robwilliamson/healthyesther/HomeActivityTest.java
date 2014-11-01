package com.robwilliamson.healthyesther;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.google.android.apps.common.testing.testrunner.GoogleInstrumentationTestRunner;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MenuAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.*;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.*;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.*;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.showNavigationDrawer(false, getInstrumentation().getTargetContext());
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
                HomeActivityAccessor.checkMenuContent(getInstrumentation().getTargetContext());
            }
        });
    }
}
