/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.edit;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.NoteTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.test.EditAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.NoteEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZonedDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class NoteEventActivityEspressoTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(
            HomeActivity.class);

    private static final String NOTE_NAME = "A Note";
    private static final String NOTE_CONTENT = "Some important notes.";

    @SuppressWarnings("RedundantThrows")
    @Before
    public void setUp() throws Exception {
        HomeActivityAccessor.setShowNavigationDrawer(false);

        Utils.Db.TestData.cleanOldData();
        Settings.INSTANCE.resetExclusionList();

        HomeActivityAccessor.AddMode.start();
    }

    @Test
    public void test_openNoteActivity() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                onView(NoteEventActivityAccessor.noteContent()).perform(click());
                NoteEventActivityAccessor.checkUnmodifiedContent();
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void test_textRetention() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        final String title = "note title";
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(title));
        Orientation.check(new Orientation.Subject() {
            @Override
            public void checkContent() {
                onView(NoteEventActivityAccessor.nameValue()).check(matches(withText(title)));
            }

            @Nonnull
            @Override
            public ActivityTestRule getActivityTestRule() {
                return mActivityRule;
            }
        });
    }

    @Test
    public void test_addNoteName_updatesEventName() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(NOTE_NAME));

        onView(EditAccessor.eventEditText()).check(matches(withText(NOTE_NAME)));
    }

    @Test
    public void test_addNoteName_enablesOk() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(NOTE_NAME));

        onView(EditAccessor.ok()).check(matches(isEnabled()));
    }

    @Test
    public void test_setNoteRelativeTime_modifiesEventTime() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        onView(EditAccessor.whenRelativeSelectorLayout()).perform(click());

        final ZonedDateTime expected = ZonedDateTime.now().minusMinutes(34);
        int hourOfDay = expected.getHour();
        final String meridianSuffix = hourOfDay > 11 ? "PM" : "AM";
        hourOfDay = hourOfDay == 12 ? 0 : hourOfDay % 12;
        String minute = String.valueOf(expected.getMinute());
        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        String expectedString = String.valueOf(hourOfDay) +
                ":" +
                minute +
                " " +
                meridianSuffix;
        onView(EditAccessor.whenTime()).check(
                matches(
                        withText(expectedString)));
    }

    @Test
    public void test_createNote_updatesDatabase() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(NOTE_NAME));
        onView(NoteEventActivityAccessor.noteContent()).perform(scrollTo());
        onView(NoteEventActivityAccessor.noteContent()).perform(typeText(NOTE_CONTENT));
        onView(EditAccessor.ok()).perform(click());

        checkDatabaseCorrectness(NOTE_NAME, NOTE_CONTENT);
    }

    private void checkDatabaseCorrectness(@Nullable String name, @Nullable String content) {
        onView(HomeActivityAccessor.AddMode.noteButton()).check(matches(isDisplayed()));

        Database db = HealthDbHelper.getDatabase();
        NoteTable.Row row = DatabaseAccessor.NOTE_TABLE.select0Or1(db, WhereContains.columnEqualling(NoteTable.NAME, name));

        assertThat(row, not(is((NoteTable.Row) null)));
        //noinspection ConstantConditions
        assertThat(row.getName(), is(name));
        assertThat(row.getNote(), is(content));
    }
}
