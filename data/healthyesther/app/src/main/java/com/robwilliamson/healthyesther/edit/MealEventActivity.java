package com.robwilliamson.healthyesther.edit;

import android.content.Intent;
import android.util.Pair;

import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

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
    protected void onResume() {
        super.onResume();

        Intent launchIntent = getIntent();
        if (launchIntent != null && launchIntent.getExtras() != null) {
            EventTable.Row row = (EventTable.Row) launchIntent.getExtras().getSerializable(HealthDatabase.EVENT_TABLE.getName());
            if (row != null) {
                onEventFromIntent(row);
            }
        }
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                MealTable.Row meal = getMealFragment().getRow();
                MealTable.Row oldMeal = getMealFragment().getOldRow();

                MealTable.PrimaryKey oldKey = oldMeal == null ? null : oldMeal.getConcretePrimaryKey();

                meal.applyTo(transaction);

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
                event.applyTo(transaction);
                EventTable.PrimaryKey eventKey = event.getNextPrimaryKey();
                MealEventTable.Row mealEvent = null;

                if (eventKey != null && oldKey != null) {
                    mealEvent = DatabaseAccessor.MEAL_EVENT_TABLE.select0Or1(database, and(
                            foreignKey(MealEventTable.EVENT_ID, eventKey.getId()),
                            foreignKey(MealEventTable.MEAL_ID, oldKey.getId())));
                }

                if (mealEvent == null) {
                    mealEvent = new MealEventTable.Row(event.getNextPrimaryKey(), meal.getNextPrimaryKey(), null, 0D);
                } else {
                    mealEvent.setNextPrimaryKey(new MealEventTable.PrimaryKey(event.getNextPrimaryKey(), meal.getNextPrimaryKey()));
                }

                mealEvent.applyTo(transaction);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
    }

    protected EditMealFragment getMealFragment() {
        return getFragment(MEAL_TAG, EditMealFragment.class);
    }

    protected EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    public void onEventFromIntent(@Nonnull final EventTable.Row event) {
        if (!event.getTypeId().equals(EventTypeTable.MEAL.getId())) {
            throw new EventTypeTable.BadEventTypeException(EventTypeTable.MEAL, event.getTypeId().getId());
        }

        getEventFragment().setRow(event);

        if (event.getConcretePrimaryKey() != null) {

            getExecutor().perform(new TransactionExecutor.Operation() {
                @Override
                public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                    final MealEventTable.Row mealEvent = DatabaseAccessor.MEAL_EVENT_TABLE.select0Or1(
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
                            MealTable.Row meal = DatabaseAccessor.MEAL_TABLE.select0Or1(
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
        if (fragment instanceof EditMealFragment) {
            EditMealFragment editMealFragment = (EditMealFragment) fragment;
            getEventFragment().suggestEventName(editMealFragment.getName());
        }

        invalidateOptionsMenu();
    }
}
