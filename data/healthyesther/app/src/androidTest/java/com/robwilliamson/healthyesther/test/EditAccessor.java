/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.robwilliamson.healthyesther.test.Espresso.both;

public class EditAccessor {
    public static Matcher<View> ok() {
        return both(withText("OK"), withId(R.id.action_modify));
    }

    public static Matcher<View> whenRelativeSelectorLayout() {
        return withId(R.id.edit_event_time_relative_layout);
    }

    @SuppressWarnings("unused")
    public static Matcher<View> whenRelativeSelector() {
        return withId(R.id.edit_event_time_relative_seekbar);
    }

    @SuppressWarnings("unused")
    public static Matcher<View> whenTitle() {
        return both(withText(R.string.when), withParent(withParent(withId(R.id.edit_event_layout))));
    }

    public static Matcher<View> whenTime() {
        return withId(R.id.edit_event_time_button);
    }

    @SuppressWarnings("unused")
    public static Matcher<View> whenDate() {
        return withId(R.id.edit_event_date_button);
    }

    @SuppressWarnings("unused")
    public static Matcher<View> eventTitle() {
        return both(withText(R.string.event_title), withParent(withParent(withId(R.id.edit_event_layout))));
    }

    public static Matcher<View> eventEditText() {
        return withId(R.id.edit_event_name);
    }
}
