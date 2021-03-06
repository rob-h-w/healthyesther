/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.view.View;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.NavigationDrawerFragment;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class HomeActivityAccessor {
    public static Matcher<View> navigationDrawer() {
        return withContentDescription("Navigate up");
    }

    public static void openNavigationDrawer() {
        onView(navigationDrawer()).perform(click());
    }

    public static void setShowNavigationDrawer(Boolean show) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        PreferenceManager.getDefaultSharedPreferences(targetContext).edit().putBoolean(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER, !show).apply();
    }

    public static void checkUnmodifiedMenuContent() {
        // Ensure we're at the home screen.
        onView(navigationDrawer()).check(matches(isDisplayed()));
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getContext());
        onView(MenuAccessor.backupToDropbox()).check(matches(isEnabled()));
        onView(MenuAccessor.restoreFromDropbox()).check(matches(isEnabled()));
        onView(MenuAccessor.settings()).check(matches(isEnabled()));
        Espresso.pressBack();
        onView(navigationDrawer()).check(matches(isDisplayed()));
    }

    public static class AddMode {
        public static void start() {
            openNavigationDrawer();
            onView(withText(NavigationDrawerFragment.NavigationDrawerMode.ADD.stringId)).perform(click());
        }

        public static Matcher<View> healthScoreButton() {
            return withId(R.id.create_health_score_event_button);
        }

        public static Matcher<View> mealScoreButton() {
            return withId(R.id.create_meal_event_button);
        }

        public static Matcher<View> medicationScoreButton() {
            return withId(R.id.create_medication_event_button);
        }

        public static Matcher<View> noteButton() {
            return withId(R.id.create_note_event_button);
        }

        public static void checkUnmodifiedContent() {
            onView(healthScoreButton()).check(matches(isClickable()));
            onView(mealScoreButton()).check(matches(isClickable()));
            onView(medicationScoreButton()).check(matches(isClickable()));
        }
    }

    public static class EditMode {
        public static void start() {
            openNavigationDrawer();
            onView(withText(NavigationDrawerFragment.NavigationDrawerMode.EDIT.stringId)).perform(click());
        }

        public static Matcher<View> eventList() {
            return withId(R.id.event_list_fragment);
        }

        public static void checkUnmodifiedContent() {
            onView(eventList()).check(matches(isEnabled()));
            onView(AddMode.healthScoreButton()).check(doesNotExist());
        }
    }
}
