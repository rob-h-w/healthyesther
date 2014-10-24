package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class HomeActivityAccessor {
    public static Matcher<View> healthScoreButton() {
        return withId(R.id.create_health_score_event_button);
    }

    public static Matcher<View> mealScoreButton() {
        return withId(R.id.create_meal_event_button);
    }

    public static Matcher<View> medicationScoreButton() {
        return withId(R.id.create_medication_event_button);
    }
}
