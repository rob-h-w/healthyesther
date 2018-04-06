/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import android.R.id;
import android.R.string;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public final class ConfirmationDialogAccessor {

    public static Matcher<View> message() {
        return withId(id.message);
    }

    public static Matcher<View> cancelButton() {
        return withText(string.cancel);
    }

    public static Matcher<View> okButton() {
        return withText(string.ok);
    }

    public static void checkUnmodifiedContent(int messageId) {
        onView(message()).check(matches(withText(messageId)));
        onView(cancelButton()).check(matches(isClickable()));
        onView(okButton()).check(matches(isClickable()));
    }
}
