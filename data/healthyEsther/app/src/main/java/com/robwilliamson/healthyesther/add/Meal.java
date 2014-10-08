package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.db.use.GetAllMealsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

public class Meal extends DbActivity {
    private final static String MEAL_TAG = "meal";
    private final static String EVENT_TAG = "event";

    public Meal() {
        super(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.base_activity_content_layout, new EditMealFragment(), MEAL_TAG)
                .add(R.id.base_activity_content_layout, new EditEventFragment(), EVENT_TAG).commit();
    }

    @Override
    protected Query getOnResumeQuery() {
        return new GetAllMealsQuery() {
            @Override
            public void onQueryComplete(Cursor cursor) {
                if (isStateLoaded()) {
                    EditMealFragment fragment = (EditMealFragment)getSupportFragmentManager().findFragmentByTag(MEAL_TAG);
                    fragment.setCursor(cursor);
                }
            }
        };
    }
}
