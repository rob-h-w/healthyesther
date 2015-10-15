package com.robwilliamson.healthyesther.edit;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.AndWhere;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MealEventActivity extends AbstractEditEventActivity
        implements EditMealFragment.Watcher {
    private final static String MEAL_TAG = "meal";
    private final static String EVENT_TAG = "event";

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(2);
        EditFragment meal;
        EditFragment event;
        if (create) {
            meal = new EditMealFragment();
            event = new EditEventFragment();
        } else {
            meal = getMealFragment();
            event = getEventFragment();
        }

        list.add(new Pair<>(meal, MEAL_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
        getExecutor().perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                final MealTable.Row meal = getMealFragment().getRow();
                meal.applyTo(database.getTransaction());
                final EventTable.Row event = getEventFragment().getRow();
                event.applyTo(database.getTransaction());
                MealEventTable.Row[] mealEvents = HealthDatabase.MEAL_EVENT_TABLE.select(database, new AndWhere(meal.getConcretePrimaryKey(), event.getConcretePrimaryKey()));
                if (mealEvents.length == 0) {
                    MealEventTable.Row mealEvent = new MealEventTable.Row(event.getConcretePrimaryKey(), meal.getConcretePrimaryKey(), null, 0);
                    mealEvent.applyTo(database.getTransaction());
                }
            }
        });
    }

    @Override
    protected int getModifyFailedStringId() {
        return R.string.could_not_insert_meal_event;
    }

    private EditMealFragment getMealFragment() {
        return getFragment(MEAL_TAG, EditMealFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    @Override
    public void onFragmentUpdate(EditMealFragment fragment) {
        getEventFragment().suggestEventName(fragment.getName());
        invalidateOptionsMenu();
    }

    @Override
    public void onQueryFailed(EditMealFragment fragment, Throwable error) {
        Toast.makeText(MealEventActivity.this, getText(R.string.could_not_get_autocomplete_text_for_meals) + "\n" + error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public QueryUser[] getQueryUsers() {
        return new QueryUser[]{
                getMealFragment()
        };
    }

    @Override
    public void onUseIntentEventData(EventData eventData) {
        getMealFragment().setEventData(eventData);
    }
}
