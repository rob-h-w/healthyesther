package com.robwilliamson.healthyesther.test;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EditScoreDialogAccessor {
    public static void checkUnmodifiedContent(HealthScoreTable.Row score) {
        onView(withId(R.id.best_value_current_value)).check(matches(withText(String.valueOf(score.getBestValue()))));
    }
}
