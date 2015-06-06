package com.robwilliamson.healthyesther.db.definition;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.data.MealData;

/**
 * Meal table contains all unique types of meal.
 */
public class Meal extends Table {
    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private final MealData mValue;

        public Modification(String name) {
            mValue = new MealData(name);
        }

        public Modification(long id, String name) {
            mValue = new MealData(name);
            mValue.set_id(id);
            mValue.setModified(false);
        }

        public Modification(MealData value) {
            setRowId(value.get_id());
            mValue = value;
        }

        @Override
        protected DataAbstraction getData() {
            return mValue;
        }

        @Override
        protected void update(SQLiteDatabase db) {
            Contract.getInstance().MEAL.update(db, mValue.asContentValues(), mValue.get_id());
        }

        @Override
        protected long insert(SQLiteDatabase db) {
            return Contract.getInstance().MEAL.insert(db, mValue);
        }
    }

    public static final String TABLE_NAME = "meal";
    public static final String _ID = "_id";
    public static final String NAME = "name";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE meal ( \n" +
                "    _id  INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    name TEXT( 140 )  NOT NULL\n" +
                "                      UNIQUE \n" +
                ");\n");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }

    public long insert(SQLiteDatabase db, MealData mealData) {
        return insert(db, mealData.asContentValues());
    }
}
