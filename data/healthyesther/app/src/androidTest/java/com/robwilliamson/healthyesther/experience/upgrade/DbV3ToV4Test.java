/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.experience.upgrade;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.test.Database;
import com.robwilliamson.healthyesther.test.HealthScoreActivityAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class DbV3ToV4Test {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);


    @Before
    public void setUp() throws Exception {
        Database.useV3Database();

        HomeActivityAccessor.setShowNavigationDrawer(false);
    }

    @After
    public void tearDown() throws Exception {
        Database.deleteDatabase();
    }

    @Test
    public void testMenuContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                HomeActivityAccessor.checkUnmodifiedMenuContent();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void testAddScoreView() {
        HomeActivityAccessor.AddMode.start();
        onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                HealthScoreActivityAccessor.checkUnmodifiedContent();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }
}
