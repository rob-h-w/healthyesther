package com.robwilliamson.healthyesther;

import android.test.ActivityInstrumentationTestCase2;

import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MenuAccessor;

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

        getActivity();
    }

    public void testActivityContents() {
        onView(HomeActivityAccessor.healthScoreButton()).check(matches(isClickable()));
        onView(HomeActivityAccessor.mealScoreButton()).check(matches(isClickable()));
        onView(HomeActivityAccessor.medicationScoreButton()).check(matches(isClickable()));
    }

    public void testMenuContents() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(MenuAccessor.backupToDropbox()).check(matches(isEnabled()));
        onView(MenuAccessor.settings()).check(matches(isEnabled()));
    }
}
