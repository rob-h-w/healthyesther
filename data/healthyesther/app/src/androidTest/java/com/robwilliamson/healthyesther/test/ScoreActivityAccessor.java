package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ScoreActivityAccessor {
    public static Matcher<View> scoreName() {
        return withId(R.id.autocomplete_name);
    }
}
