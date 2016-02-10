package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EditScoreFragmentAccessor {
    public static void checkUnmodifiedContent(HealthScoreTable.Row score) {
        onView(name()).check(matches(withText(score.getName())));
    }

    public static Matcher<View> name() {
        return withId(R.id.autocomplete_name);
    }
}
