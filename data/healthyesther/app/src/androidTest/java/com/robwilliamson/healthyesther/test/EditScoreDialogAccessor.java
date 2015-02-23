package com.robwilliamson.healthyesther.test;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.healthyesther.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EditScoreDialogAccessor {
    public static void checkUnmodifiedContent(HealthScore.Score score) {
        onView(withId(R.id.best_value_current_value)).check(matches(withText(String.valueOf(score.bestValue))));
    }
}
