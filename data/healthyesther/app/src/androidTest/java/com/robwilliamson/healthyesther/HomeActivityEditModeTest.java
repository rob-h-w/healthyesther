/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

@RunWith(AndroidJUnit4.class)
public class HomeActivityEditModeTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    @Before
    public void setUp() throws Exception {
        HomeActivityAccessor.setShowNavigationDrawer(false);

        com.robwilliamson.healthyesther.db.Utils.Db.TestData.cleanOldData();

        HomeActivityAccessor.EditMode.start();
    }

    @Test
    public void testEditModeContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                HomeActivityAccessor.EditMode.checkUnmodifiedContent();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }
}
