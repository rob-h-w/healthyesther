package com.robwilliamson.healthyesther.edit;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.robwilliamson.healthyesther.db.includes.WhereContains.and;
import static com.robwilliamson.healthyesther.db.includes.WhereContains.foreignKey;

public class MealEventActivity extends AbstractEditEventActivity
        implements BaseFragment.Watcher {
    private final static String MEAL_TAG = "meal";
    private final static String EVENT_TAG = "event";

    @Override
    protected List<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(2);
        EditFragment meal;
        EditFragment event;
        if (create) {
            meal = new EditMealFragment();
            DateTime now = DateTime.from(com.robwilliamson.healthyesther.db.Utils.Time.localNow());
            event = EditEventFragment.getInstance(new EventTable.Row(
                    EventTypeTable.MEAL.getId(),
                    now,
                    now,
                    null,
                    null));
        } else {
            meal = getMealFragment();
            event = getEventFragment();
        }

        list.add(new Pair<>(meal, MEAL_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                MealTable.Row meal = getMealFragment().getRow();
                if (meal == null) {
                    throw new InvalidParameterException("The meal name should be set");
                }
                meal.applyTo(database.getTransaction());

                EventTable.Row event = getEventFragment().getRow();
                if (event == null) {
                    event = new EventTable.Row(
                            EventTypeTable.MEAL.getId(),
                            DateTimeConverter.now(),
                            DateTimeConverter.now(),
                            null,
                            null);
                }
                event.setTypeId(EventTypeTable.MEAL.getId());
                event.applyTo(database.getTransaction());
                MealEventTable.Row[] mealEvents = HealthDatabase.MEAL_EVENT_TABLE.select(database, and(
                        foreignKey(MealEventTable.EVENT_ID, event.getNextPrimaryKey().getId()),
                        foreignKey(MealEventTable.MEAL_ID, meal.getNextPrimaryKey().getId())));
                if (mealEvents.length == 0) {
                    MealEventTable.Row mealEvent = new MealEventTable.Row(event.getNextPrimaryKey(), meal.getNextPrimaryKey(), null, 0D);
                    mealEvent.applyTo(database.getTransaction());
                }

                finish();
            }
        };
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
    }

    @Override
    protected int getModifyFailedStringId() {
        return R.string.could_not_insert_meal_event;
    }

    protected EditMealFragment getMealFragment() {
        return getFragment(MEAL_TAG, EditMealFragment.class);
    }

    protected EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    public void onEventFromIntent(@Nonnull final EventTable.Row event) {
        if (event.getTypeId() != EventTypeTable.MEAL.getId()) {
            throw new EventTypeTable.BadEventTypeException(EventTypeTable.MEAL, event.getTypeId().getId());
        }

        getEventFragment().setRow(event);

        if (event.getConcretePrimaryKey() != null) {

            getExecutor().perform(new TransactionExecutor.Operation() {
                @Override
                public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                    final MealEventTable.Row mealEvent = HealthDatabase.MEAL_EVENT_TABLE.select0Or1(
                            database,
                            new Where() {
                                @Nullable
                                @Override
                                public String getWhere() {
                                    return MealEventTable.EVENT_ID + " = " + event.getConcretePrimaryKey().getId();
                                }
                            }
                    );

                    if (mealEvent == null) {
                        // There's nothing more to do here.
                        return;
                    }

                    getExecutor().perform(new TransactionExecutor.Operation() {
                        @Override
                        public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                            MealTable.Row meal = HealthDatabase.MEAL_TABLE.select0Or1(
                                    database,
                                    new Where() {
                                        @Nullable
                                        @Override
                                        public String getWhere() {
                                            return MealTable._ID + " = " + mealEvent.getConcretePrimaryKey().getMealId().getId();
                                        }
                                    }
                            );

                            if (meal != null) {
                                getMealFragment().setRow(meal);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onFragmentUpdated(BaseFragment fragment) {
        invalidateOptionsMenu();
    }
}
