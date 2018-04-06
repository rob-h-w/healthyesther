/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.ConfirmationDialogAccessor;
import com.robwilliamson.healthyesther.test.Database;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MenuAccessor;
import com.robwilliamson.healthyesther.test.Orientation;
import com.robwilliamson.healthyesther.test.rule.Retry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.robwilliamson.healthyesther.test.HomeActivityAccessor.AddMode.healthScoreButton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class HomeActivityEspressoTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    @Rule
    public Retry mRetry = new Retry(2);

    @SuppressWarnings("RedundantThrows")
    @Before
    public void setUp() throws Exception {
        HomeActivityAccessor.setShowNavigationDrawer(false);

        Utils.Db.TestData.cleanOldData();
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
    public void testBackupToDropbox() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(MenuAccessor.backupToDropbox()).perform(click());
        onView(healthScoreButton()).check(matches(isDisplayed()));
    }

    @Test
    public void testRestoreFromDropbox() {
        final int expectedCount = populateSomeDropboxData();

        // Backup the data to Dropbox.
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(MenuAccessor.backupToDropbox()).perform(click());

        // Ensure we're back in the home activity.
        onView(HomeActivityAccessor.navigationDrawer()).check(matches(isDisplayed()));

        // Remove it from current use.
        Database.deleteDatabase();

        // Check there are no entries.
        final int emptyCount = Database.countEntries();
        assertEquals(0, emptyCount);

        // Note - we retry restoring from Dropbox because this is an integration test that really
        // uses Dropbox to store the DB. Since Dropbox is eventually consistent, it's possible that
        // DB doesn't have all its ducks in a row when we ask for the restored database. Usually a
        // single retry after a little wait is enough to remedy this. Do 2 retries just in case.
        int tries = 3;
        int finalCount = 0;
        while(tries > 0) {
            tries--;

            // Restore from Dropbox.
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(MenuAccessor.restoreFromDropbox()).perform(click());

            // Confirm.
            onView(ConfirmationDialogAccessor.okButton()).perform(click());

            // Ensure we're back in the home activity.
            onView(HomeActivityAccessor.AddMode.healthScoreButton()).check(matches(isDisplayed()));
            onView(HomeActivityAccessor.AddMode.mealScoreButton()).check(matches(isDisplayed()));

            finalCount = Database.countEntries();

            if (finalCount > 0) {
                break;
            }

            SystemClock.sleep(2000);
        }

        // Check we have the same number of entries as before.
        assertEquals(expectedCount, finalCount);
    }

    @Test
    public void testConfirmationDialogOrientation() {
        populateSomeDropboxData();

        // Open the dialog.
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(MenuAccessor.restoreFromDropbox()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                ConfirmationDialogAccessor.checkUnmodifiedContent(
                        R.string.confirm_restore_from_dropbox_message);
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void testConfirmationDialogCancel() {
        populateSomeDropboxData();

        // Remove it from current use.
        Database.deleteDatabase();

        // Check there are no entries.
        int emptyCount = Database.countEntries();
        assertEquals(0, emptyCount);

        // Restore from Dropbox.
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(MenuAccessor.restoreFromDropbox()).perform(click());

        // Cancel.
        onView(ConfirmationDialogAccessor.cancelButton()).perform(click());

        // Check there are still no entries.
        emptyCount = Database.countEntries();
        assertEquals(0, emptyCount);
    }

    private int populateSomeDropboxData() {
        // Remove original data.
        Utils.Db.TestData.cleanOldData();

        // Create some fake data
        Utils.Db.TestData.insertFakeData();

        // Check that we have some entries.
        final int expectedCount = Database.countEntries();
        assertTrue("expected " + expectedCount + " to be greater than 0.", expectedCount > 0);

        return expectedCount;
    }
}
