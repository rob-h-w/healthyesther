package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class TrackAnotherScoreDialogAccessor {
    public static Matcher<View> healthScoreEditBox() {
        return EnterValueNameAccessor.valueNameEditBox();
    }

    public static Matcher<View> okButton() {
        return withId(R.id.ok_button);
    }
}
