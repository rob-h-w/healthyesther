package com.robwilliamson.healthyesther;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.ConfirmationDialogAccessor;
import com.robwilliamson.healthyesther.test.Database;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MenuAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import java.io.File;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.robwilliamson.healthyesther.test.HomeActivityAccessor.AddMode.healthScoreButton;
import static org.hamcrest.Matchers.not;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final String DROPBOX_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/Android/data/com.dropbox.android";
    private static final String DB_PATH = DROPBOX_PATH + "/files/scratch";

    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData();

        if (Utils.File.exists(DROPBOX_PATH)) {
            File file;

            if (Utils.File.exists(Utils.File.Dropbox.dbFile())) {
                file = new File(Utils.File.Dropbox.dbFile());
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            file = new File(DROPBOX_PATH);
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        getActivity();
    }

    public void testMenuContents() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityTest.this;
            }

            @Override
            public void checkContent() {
                HomeActivityAccessor.checkUnmodifiedMenuContent(getInstrumentation().getTargetContext());
            }
        });
    }

    public void testBackupToDropboxDisabled() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.backupToDropbox()).check(matches(isDisplayed()));
        onView(MenuAccessor.backupToDropbox()).check(matches(not(isClickable())));
    }

    public void testBackupToDropbox() {
        Utils.File.mkdirs(DB_PATH);
        assertTrue(Utils.File.exists(DB_PATH));
        assertFalse(Utils.File.exists(Utils.File.Dropbox.dbFile()));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.backupToDropbox()).perform(click());
        onView(healthScoreButton()).check(matches(isDisplayed()));
        assertTrue(Utils.File.exists(Utils.File.Dropbox.dbFile()));
    }

    public void testRestoreFromDropboxDisabled() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.restoreFromDropbox()).check(matches(isDisplayed()));
        onView(MenuAccessor.restoreFromDropbox()).check(matches(not(isClickable())));
    }

    public void testRestoreFromDropbox() throws Exception {
        final int expectedCount = enableRestoreDropbox();

        // Remove it from current use.
        Database.deleteDatabase(getInstrumentation().getTargetContext());

        // Check there are no entries.
        final int emptyCount = Database.countEntries();
        assertEquals(0, emptyCount);

        // Restore from Dropbox.
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.restoreFromDropbox()).perform(click());

        // Confirm.
        onView(ConfirmationDialogAccessor.okButton()).perform(click());

        // Check we have the same number of entries as before.
        final int finalCount = Database.countEntries();
        assertEquals(expectedCount, finalCount);
    }

    public void testConfirmationDialogOrientation() {
        enableRestoreDropbox();

        // Open the dialog.
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.restoreFromDropbox()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return HomeActivityTest.this;
            }

            @Override
            public void checkContent() {
                ConfirmationDialogAccessor.checkUnmodifiedContent(
                        R.string.confirm_restore_from_dropbox_message);
            }
        });
    }

    public void testConfirmationDialogCancel() throws Exception {
        enableRestoreDropbox();

        // Remove it from current use.
        Database.deleteDatabase(getInstrumentation().getTargetContext());

        // Check there are no entries.
        int emptyCount = Database.countEntries();
        assertEquals(0, emptyCount);

        // Restore from Dropbox.
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.restoreFromDropbox()).perform(click());

        // Cancel.
        onView(ConfirmationDialogAccessor.cancelButton()).perform(click());

        // Check there are still no entries.
        emptyCount = Database.countEntries();
        assertEquals(0, emptyCount);
    }

    private int enableRestoreDropbox() {
        Utils.File.mkdirs(DB_PATH);

        // Remove original data.
        Utils.Db.TestData.cleanOldData();

        // Create some fake data
        Utils.Db.TestData.insertFakeData();

        // Check that we have some entries.
        final int expectedCount = Database.countEntries();
        assertTrue("expected " + expectedCount + " to be greater than 0.", expectedCount > 0);

        // Put it in the dropbox folder.
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(MenuAccessor.backupToDropbox()).perform(click());

        // Check that worked.
        assertTrue(Utils.File.exists(Utils.File.Dropbox.dbFile()));

        return expectedCount;
    }
}
