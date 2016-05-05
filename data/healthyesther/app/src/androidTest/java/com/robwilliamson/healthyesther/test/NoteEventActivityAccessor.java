/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.robwilliamson.healthyesther.test.Espresso.both;

public class NoteEventActivityAccessor {

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

    public static Matcher<View> noteContent() {
        return withId(R.id.note_content);
    }
}
