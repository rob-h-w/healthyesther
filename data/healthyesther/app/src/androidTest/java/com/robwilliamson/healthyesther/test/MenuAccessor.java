package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class MenuAccessor {

    public static Matcher<View> backupToDropbox() {
        return withText(R.string.action_backup_to_dropbox);
    }

    public static Matcher<View> settings() {
        return withText(R.string.action_settings);
    }
}
