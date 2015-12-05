package com.robwilliamson.healthyesther.edit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.NoteTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.test.EditEventAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.NoteEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import javax.annotation.Nullable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class NoteEventActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    public NoteEventActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData();
        Settings.INSTANCE.resetExclusionList();

        getActivity();

        HomeActivityAccessor.AddMode.start();
    }

    public void testOpenNoteActivity() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return NoteEventActivityTest.this;
            }

            @Override
            public void checkContent() {
                NoteEventActivityAccessor.checkUnmodifiedContent();
            }
        });
    }

    public void testTextRetention() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        final String title = "note title";
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(title));
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return NoteEventActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(NoteEventActivityAccessor.nameValue()).check(matches(withText(title)));
            }
        });
    }

    public void test_addNoteName_updatesEventName() {
        onView(HomeActivityAccessor.AddMode.noteButton()).perform(click());
        String noteName = "A Note";
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(noteName));

        onView(EditEventAccessor.eventEditText()).check(matches(withText(noteName)));
    }

    private void checkDatabaseCorrectness(@Nullable String name) {
        onView(HomeActivityAccessor.AddMode.noteButton()).check(matches(isDisplayed()));

        Database db = HealthDbHelper.getDatabase();
        NoteTable.Row row = DatabaseAccessor.NOTE_TABLE.select0Or1(db, WhereContains.columnEqualling(NoteTable.NAME, name));

        assertThat(row, not(is((NoteTable.Row) nullValue())));
    }
}
