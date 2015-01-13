package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class EnterValueNameAccessor {
    public static Matcher<View> valueNameEditBox() {
        return withId(R.id.autocomplete_name);
    }
}
