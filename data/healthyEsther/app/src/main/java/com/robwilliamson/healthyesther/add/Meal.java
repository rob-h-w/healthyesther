package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.db.use.GetAllMealsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

public class Meal extends DbActivity {
    private EditEventFragment mEditEventFragment;
    private EditMealFragment mEditMealFragment;
    private Cursor mAllMeals;

    public Meal() {
        super(false);
        mEditEventFragment = new EditEventFragment();
        mEditMealFragment = new EditMealFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (savedInstanceState != null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.add_activity_content_layout, mEditMealFragment)
                .add(R.id.add_activity_content_layout, mEditEventFragment).commit();
    }

    @Override
    protected Query getOnResumeQuery() {
        return new GetAllMealsQuery() {
            @Override
            public void onQueryComplete(Cursor cursor) {
                mAllMeals = cursor;
            }
        };
    }
}
