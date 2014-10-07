package com.robwilliamson.healthyesther.add;

import android.net.Uri;
import android.os.Bundle;

import com.robwilliamson.db.Utils;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;

import java.util.Calendar;

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
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, mEditEventFragment).commit();
        // TODO: Remove this hack and populate the "when" field from a query result.
        mEditEventFragment.setWhen(Calendar.getInstance(Utils.Time.UTC));
    }

    @Override
    protected Query getOnResumeQuery() {
        return null;
    }
}
