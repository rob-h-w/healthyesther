package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class TrackAnotherScoreDialogAccessor {
    public static Matcher<View> healthScoreEditBox() {
        return EnterValueNameAccessor.valueNameEditBox();
    }

    public static Matcher<View> okButton() {
        return withId(R.id.ok_button);
    }
}
