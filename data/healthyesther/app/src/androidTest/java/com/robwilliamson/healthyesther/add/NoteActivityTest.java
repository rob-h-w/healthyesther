package com.robwilliamson.healthyesther.add;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.db.HealthDbHelper;
import com.robwilliamson.db.Utils;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.test.NoteActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import java.util.HashSet;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class NoteActivityTest extends ActivityInstrumentationTestCase2<NoteActivity> {
    public NoteActivityTest() {
        super(NoteActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Utils.Db.TestData.cleanOldData(HealthDbHelper.getInstance(getInstrumentation().getTargetContext()).getWritableDatabase());
        Settings.INSTANCE.setDefaultEditScoreExclusionList(new HashSet<String>());

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
