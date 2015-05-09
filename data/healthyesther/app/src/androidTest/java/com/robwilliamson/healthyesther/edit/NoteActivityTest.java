package com.robwilliamson.healthyesther.edit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.NoteActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class NoteActivityTest extends ActivityInstrumentationTestCase2<NoteActivity> {
    public NoteActivityTest() {
        super(NoteActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());
        Settings.INSTANCE.resetExclusionList();

        getActivity();
    }

    public void testOpenNoteActivity() {
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return NoteActivityTest.this;
            }

            @Override
            public void checkContent() {
                NoteActivityAccessor.checkUnmodifiedContent();
            }
        });
    }

    public void testTextRetention() {
        final String title = "note title";
        onView(NoteActivityAccessor.nameValue()).perform(typeText(title));
        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return NoteActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(NoteActivityAccessor.nameValue()).check(matches(withText(title)));
            }
        });
    }
}
