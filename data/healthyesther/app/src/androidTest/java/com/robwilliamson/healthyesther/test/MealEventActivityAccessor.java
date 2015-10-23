package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MealEventActivityAccessor {

    public static Matcher<View> dishTitle() {
        return withId(R.id.meal_name_title);
    }

    public static Matcher<View> dishName() {
        return withId(R.id.meal_name);
    }
}