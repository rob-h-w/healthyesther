package com.robwilliamson.healthyesther.add;

import android.os.Bundle;

import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;

public class Meal extends DbActivity {
    private EditEventFragment mEditEventFragment;

    public Meal() {
        super(false);
        mEditEventFragment = new EditEventFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (savedInstanceState != null) {
            return;
        }

        getSupportFragmentManager().beginTransaction().add(android.R.id.content, mEditEventFragment).commit();
    }

    @Override
    protected Query getOnResumeQuery() {
        return null;
    }
}
