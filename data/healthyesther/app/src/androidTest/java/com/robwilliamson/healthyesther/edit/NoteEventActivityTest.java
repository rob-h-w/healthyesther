package com.robwilliamson.healthyesther.edit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.EditEventAccessor;
import com.robwilliamson.healthyesther.test.NoteEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class NoteEventActivityTest extends ActivityInstrumentationTestCase2<NoteEventActivity> {
    public NoteEventActivityTest() {
        super(NoteEventActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Utils.Db.TestData.cleanOldData();
        Settings.INSTANCE.resetExclusionList();

        getActivity();
    }

    public void testOpenNoteActivity() {
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
        String noteName = "A Note";
        onView(NoteEventActivityAccessor.nameValue()).perform(typeText(noteName));

        onView(EditEventAccessor.eventEditText()).check(matches(withText(noteName)));
    }
}
