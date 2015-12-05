package com.robwilliamson.healthyesther.test;

import android.content.Context;
import android.preference.PreferenceManager;
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
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class HomeActivityAccessor {
    public static void openNavigationDrawer() {
        onView(withContentDescription("Esther's Health App, Open navigation drawer")).perform(click());
    }

    public static void setShowNavigationDrawer(Boolean show, Context targetContext) {
        PreferenceManager.getDefaultSharedPreferences(targetContext).edit().putBoolean(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER, !show).apply();
    }

    public static void checkUnmodifiedMenuContent(Context context) {
        openActionBarOverflowOrOptionsMenu(context);
        onView(MenuAccessor.backupToDropbox()).check(matches(isEnabled()));
        onView(MenuAccessor.restoreFromDropbox()).check(matches(not(isEnabled())));
        onView(MenuAccessor.settings()).check(matches(isEnabled()));
        Espresso.pressBack();
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
