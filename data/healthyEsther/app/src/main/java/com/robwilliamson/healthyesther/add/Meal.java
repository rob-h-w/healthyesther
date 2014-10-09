package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.MealEvent;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.GetAllMealsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import java.util.HashMap;
import java.util.Set;

public class Meal extends DbActivity
        implements EditMealFragment.Watcher, EditEventFragment.Watcher {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_modify) {
            // Write to the DB and go back.
            query(new Query() {
                @Override
                public Cursor query(SQLiteDatabase db) {
                    db.beginTransaction();
                    try {
                        com.robwilliamson.db.definition.Meal.Modification meal = (com.robwilliamson.db.definition.Meal.Modification) getMealFragment().getModification();
                        Event.Modification event = (Event.Modification) getEventFragment().getModification();
                        MealEvent.Modification mealEvent = new MealEvent.Modification(meal, event, null, null);
                        mealEvent.modify(db);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    return null;
                }

                @Override
                public void postQueryProcessing(Cursor cursor) {

                }

                @Override
                public void onQueryComplete(Cursor cursor) {
                    finish();
                }

                @Override
                public void onQueryFailed(Throwable error) {
                    Toast.makeText(Meal.this, getText(R.string.could_not_insert_meal_event), Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getMealFragment().validate() && getEventFragment().validate()) {
            return true;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected Query getOnResumeQuery() {
        return new GetAllMealsQuery() {
            HashMap<String, Long> mSuggestionIds;
            @Override
            public void postQueryProcessing(Cursor cursor) {
                mSuggestionIds = com.robwilliamson.db.Utils.Db.cursorToSuggestionList(cursor,
                        com.robwilliamson.db.definition.Meal.NAME,
                        com.robwilliamson.db.definition.Meal._ID);
            }

            @Override
            public void onQueryComplete(Cursor cursor) {
                getMealFragment().setSuggestionIds(mSuggestionIds);
            }

            @Override
            public void onQueryFailed(Throwable error) {
                Toast.makeText(Meal.this, getText(R.string.could_not_get_autocomplete_text_for_meals), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private EditMealFragment getMealFragment() {
        return Utils.View.getTypeSafeFragment(getSupportFragmentManager(), MEAL_TAG);
    }

    private EditEventFragment getEventFragment() {
        return Utils.View.getTypeSafeFragment(getSupportFragmentManager(), EVENT_TAG);
    }

    @Override
    public void onFragmentUpdate(EditMealFragment fragment) {
        invalidateOptionsMenu();

        if (getEventFragment().getUserEditedEventName()) {
            return;
        }

        if (getEventFragment().getName().isEmpty()) {
            getEventFragment().setUserEditedEventName(false);
        }

        getEventFragment().setName(fragment.getName());
    }

    @Override
    public void onFragmentUpdate(EditEventFragment fragment) {
        invalidateOptionsMenu();
    }
}
