package com.robwilliamson.healthyesther.test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

public class UserActions {
    public static void openAddHealthScore() {
        onView(HomeActivityAccessor.healthScoreButton()).perform(click());
    }
}
