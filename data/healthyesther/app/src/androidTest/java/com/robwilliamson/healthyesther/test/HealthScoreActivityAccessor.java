package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.robwilliamson.healthyesther.test.Espresso.both;

public class HealthScoreActivityAccessor {
    public static Matcher<View> trackAnotherScoreButton() {
        return withId(R.id.add_value_layout);
    }

    public static Matcher<View> scoreTitle(String title) {
        return both(withId(R.id.score_name_title), withText(title));
    }

    public static Matcher<View> score(String title, String min, String max) {
        return both(
                withId(R.id.edit_score_layout),
                both(
                        withChild(scoreTitle(title)),
                        withChild(both(
                                withChild(both(
                                        withId(R.id.score_minimum_label),
                                        withText(min))),
                                withChild(both(
                                        withId(R.id.score_maximum_label),
                                        withText(max)))))));
    }

    public static Matcher<View> editScoreGroupLayout() {
        return withId(R.id.edit_score_group_layout);
    }

    public static void editScore(String title) {
        onView(HealthScoreActivityAccessor.scoreTitle(title)).perform(longClick());
        onView(withText("Edit")).perform(click());
    }

    public static void hideScore(String title) {
        onView(HealthScoreActivityAccessor.scoreTitle(title)).perform(longClick());
        onView(withText("Hide")).perform(click());
    }

    public static void checkUnmodifiedContent() {
        onView(trackAnotherScoreButton()).check(matches(isDisplayed()));
        checkUnmodifiedScore("Happiness", "Sad", "Happy");
        checkUnmodifiedScore("Energy", "Tired", "Energetic");
        checkUnmodifiedScore("Drowsiness", "Sleepy", "Awake");
    }

    private static void checkUnmodifiedScore(String title, String min, String max) {
        onView(score(title, min, max)).check(matches(isEnabled()));
    }
}
