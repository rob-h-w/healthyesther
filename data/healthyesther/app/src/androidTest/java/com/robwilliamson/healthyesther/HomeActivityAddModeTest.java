package com.robwilliamson.healthyesther;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

@RunWith(AndroidJUnit4.class)
public class HomeActivityAddModeTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    @Before
    public void setUp() throws Exception {
        HomeActivityAccessor.setShowNavigationDrawer(false);

        Utils.Db.TestData.cleanOldData();

        HomeActivityAccessor.AddMode.start();
    }

    @Test
    public void testAddModeContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                HomeActivityAccessor.AddMode.checkUnmodifiedContent();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }
}
