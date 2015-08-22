package com.robwilliamson.healthyesther.db.definition;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.MealEventData;

/**
 * Table containing details of a meal event.
 */
public class MealEvent extends Table {
    public static final long EVENT_TYPE_ID = Event.Type.MEAL.id();
    public static final String TABLE_NAME = "meal_event";
    public static final String MEAL_ID = "meal_id";
    public static final String EVENT_ID = "event_id";
    public static final String AMOUNT = "amount";
    public static final String UNITS_ID = "units_id";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE meal_event ( \n" +
                "    meal_id       NOT NULL\n" +
                "                  REFERENCES meal ( _id ) ON DELETE CASCADE\n" +
                "                                          ON UPDATE CASCADE,\n" +
                "    event_id      NOT NULL\n" +
                "                  REFERENCES event ( _id ) ON DELETE CASCADE\n" +
                "                                           ON UPDATE CASCADE,\n" +
                "    amount   REAL,\n" +
                "    units_id      REFERENCES units ( _id ) ON DELETE SET NULL\n" +
                "                                           ON UPDATE CASCADE,\n" +
                "    PRIMARY KEY ( meal_id, event_id ) \n" +
                ");\n");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }

    public long insert(SQLiteDatabase db, MealEventData value) {
        return insert(db, value.asContentValues());
    }

    public static class Modification extends
            com.robwilliamson.healthyesther.db.definition.Modification {
        private Meal.Modification mMeal;
        private Event.Modification mEvent;
        private MealEventData mValue;

        public Modification(
                Meal.Modification meal,
                Event.Modification event,
                Double amount,
                Long unitsId) {
            mMeal = meal;
            mEvent = event;
            mValue = new MealEventData(meal, event, amount, unitsId);
            mEvent.setTypeId(EVENT_TYPE_ID);
        }

        @Override
        protected void onStartModify(SQLiteDatabase db) {
            mEvent.setTypeId(MealEvent.EVENT_TYPE_ID);
            mEvent.modify(db);
            mMeal.modify(db);
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
            final String where = MEAL_ID + " = " + mValue.getMealId() +
                    " AND " +
                    EVENT_ID + " = " + mValue.getEventId();
            Contract.getInstance().MEAL_EVENT.update(db, mValue.asContentValues(), where, 1, 1);
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().MEAL_EVENT.insert(db, mValue);
        }
    }
}
