package com.robwilliamson.healthyesther.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.NavigationDrawerFragment;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isClickable;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isEnabled;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class HomeActivityAccessor {
    public static Matcher<View> healthScoreButton() {
        return withId(R.id.create_health_score_event_button);
    }

    public static void showNavigationDrawer(Boolean show, Context targetContext) {
        PreferenceManager.getDefaultSharedPreferences(targetContext).edit().putBoolean(NavigationDrawerFragment.PREF_USER_LEARNED_DRAWER, !show).apply();
    }

    public static Matcher<View> mealScoreButton() {
        return withId(R.id.create_meal_event_button);
    }

    public static Matcher<View> medicationScoreButton() {
        return withId(R.id.create_medication_event_button);
    }

    public static void checkUnmodifiedContent() {
        onView(healthScoreButton()).check(matches(isClickable()));
        onView(mealScoreButton()).check(matches(isClickable()));
        onView(medicationScoreButton()).check(matches(isClickable()));
    }

    public static void checkMenuContent(Context targetContext) {
        onView(MenuAccessor.backupToDropbox()).check(matches(isEnabled()));
        onView(MenuAccessor.settings()).check(matches(isEnabled()));
    }
}
