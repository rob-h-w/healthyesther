
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MealEventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MealEventTablePrimaryKey {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mMealId;

        public MealEventTablePrimaryKey(MealEventTable.MealEventTablePrimaryKey other) {
            mEventId = other.mEventId;
            mMealId = other.mMealId;
        }

        public MealEventTablePrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mealId) {
            mEventId = eventId;
            mMealId = mealId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setMealId(com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mealId) {
            mMealId = mealId;
        }

        public com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey getMealId() {
            return mMealId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MealEventTable.MealEventTablePrimaryKey)) {
                return false;
            }
            MealEventTable.MealEventTablePrimaryKey theMealEventTablePrimaryKey = ((MealEventTable.MealEventTablePrimaryKey) other);
            if (theMealEventTablePrimaryKey.mEventId!= mEventId) {
                if ((theMealEventTablePrimaryKey.mEventId == null)||(!theMealEventTablePrimaryKey.mEventId.equals(mEventId))) {
                    return false;
                }
            }
            if (theMealEventTablePrimaryKey.mMealId!= mMealId) {
                if ((theMealEventTablePrimaryKey.mMealId == null)||(!theMealEventTablePrimaryKey.mMealId.equals(mMealId))) {
                    return false;
                }
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row
        extends BaseTransactable
    {

        private double mAmount;
        private com.robwilliamson.healthyesther.db.generated.UnitsTable.UnitsTablePrimaryKey mUnitsId;
        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mMealId;
        private MealEventTable.MealEventTablePrimaryKey mPrimaryKey = null;
        public final static ArrayList COLUMN_NAMES = new ArrayList(4);

        static {
            COLUMN_NAMES.add("amount");
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("meal_id");
            COLUMN_NAMES.add("units_id");
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.MealTable.Row mealTableRow, Double amount, com.robwilliamson.healthyesther.db.generated.UnitsTable.Row unitsTableRow) {
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mealTablePrimaryKey, Double amount, com.robwilliamson.healthyesther.db.generated.UnitsTable.UnitsTablePrimaryKey unitsTablePrimaryKey) {
        }

        public void setAmount(double amount) {
            mAmount = amount;
        }

        public double getAmount() {
            return mAmount;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setMealId(com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mealId) {
            mMealId = mealId;
        }

        public com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey getMealId() {
            return mMealId;
        }

        public void setUnitsId(com.robwilliamson.healthyesther.db.generated.UnitsTable.UnitsTablePrimaryKey unitsId) {
            mUnitsId = unitsId;
        }

        public com.robwilliamson.healthyesther.db.generated.UnitsTable.UnitsTablePrimaryKey getUnitsId() {
            return mUnitsId;
        }

        @Override
        public void insert(Transaction transaction) {
        }

    }

}
