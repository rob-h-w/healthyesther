package com.robwilliamson.healthyesther.test;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;

public class UserActions {
    public static void openAddHealthScore() {
        onView(HomeActivityAccessor.healthScoreButton()).perform(click());
    }
}
