package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.MealEvent;
import com.robwilliamson.db.use.GetAllMealsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class Meal extends AbstractAddActivity
        implements EditMealFragment.Watcher, EditEventFragment.Watcher {
    private final static String MEAL_TAG = "meal";
    private final static String EVENT_TAG = "event";

    public Meal() {
        super(true);
    }

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<Pair<EditFragment, String>>(2);
        EditFragment meal = null;
        EditFragment event = null;
        if (create) {
            meal = new EditMealFragment();
            event = new EditEventFragment();
        } else {
            meal = getMealFragment();
            event = getEventFragment();
        }

        list.add(new Pair<EditFragment, String>(meal, MEAL_TAG));
        list.add(new Pair<EditFragment, String>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
        com.robwilliamson.db.definition.Meal.Modification meal = (com.robwilliamson.db.definition.Meal.Modification) getMealFragment().getModification();
        Event.Modification event = (Event.Modification) getEventFragment().getModification();
        MealEvent.Modification mealEvent = new MealEvent.Modification(meal, event, null, null);
        mealEvent.modify(db);
    }

    @Override
    protected int getModifyFailedStringId() {
        return R.string.could_not_insert_meal_event;
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
