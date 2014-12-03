package com.robwilliamson.healthyesther.test;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.healthyesther.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

public class EditScoreDialogAccessor {
    public static void checkUnmodifiedContent(HealthScore.Score score) {
        onView(withId(R.id.best_value_current_value)).check(matches(withText(String.valueOf(score.bestValue))));
    }
}
