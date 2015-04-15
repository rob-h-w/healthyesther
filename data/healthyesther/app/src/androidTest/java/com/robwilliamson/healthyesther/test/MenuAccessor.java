package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MenuAccessor {

    public static Matcher<View> backupToDropbox() {
        return withChild(withChild(withText(R.string.action_backup_to_dropbox)));
    }

    public static Matcher<View> restoreFromDropbox() {
        return withChild(withChild(withText(R.string.action_restore_from_dropbox)));
    }

    public static Matcher<View> settings() {
        return withChild(withChild(withText(R.string.action_settings)));
    }
}
