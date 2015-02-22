package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.robwilliamson.healthyesther.test.Espresso.both;
import static com.robwilliamson.healthyesther.test.Orientation.check;

public class NoteActivityAccessor {

    public static Matcher<View> nameTitle() {
        return withId(R.id.note_name_title);
    }

    public static Matcher<View> nameValue() {
        return withId(R.id.note_name);
    }

    public static void checkUnmodifiedContent() {
        onView(nameTitle()).check(matches(isDisplayed()));
        onView(nameValue()).check(matches(both(isDisplayed(), isEnabled())));
    }
}
