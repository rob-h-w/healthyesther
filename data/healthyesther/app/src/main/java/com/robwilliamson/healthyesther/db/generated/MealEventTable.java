
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
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
    public final static class PrimaryKey
        implements Key
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey mMealId;

        public PrimaryKey(MealEventTable.PrimaryKey other) {
            mEventId = other.mEventId;
            mMealId = other.mMealId;
        }

        public PrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey mealId) {
            mEventId = eventId;
            mMealId = mealId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey getEventId() {
            return mEventId;
        }

        public void setMealId(com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey mealId) {
            mMealId = mealId;
        }

        public com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey getMealId() {
            return mMealId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MealEventTable.PrimaryKey)) {
                return false;
            }
            MealEventTable.PrimaryKey thePrimaryKey = ((MealEventTable.PrimaryKey) other);
            if (!(((mEventId == null)&&(thePrimaryKey.mEventId == null))||((mEventId!= null)&&mEventId.equals(thePrimaryKey.mEventId)))) {
                return false;
            }
            if (!(((mMealId == null)&&(thePrimaryKey.mMealId == null))||((mMealId!= null)&&mMealId.equals(thePrimaryKey.mMealId)))) {
                return false;
            }
            return true;
        }

        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(event_id = ");
            where.append(mEventId.getId());
            where.append(")");
            where.append(" AND (meal_id = ");
            where.append(mMealId.getId());
            where.append(")");
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row
        extends BaseRow<MealEventTable.PrimaryKey>
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey mMealId;
        private com.robwilliamson.healthyesther.db.generated.MealTable.Row mMealIdRow;
        private com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey mUnitsId;
        private com.robwilliamson.healthyesther.db.generated.UnitsTable.Row mUnitsIdRow;
        private double mAmount;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(4);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("meal_id");
            COLUMN_NAMES.add("units_id");
            COLUMN_NAMES.add("amount");
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey mealId, com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey unitsId, double amount) {
            setPrimaryKey(new MealEventTable.PrimaryKey(eventId, mealId));
            mUnitsId = unitsId;
            mAmount = amount;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MealTable.Row mealId, com.robwilliamson.healthyesther.db.generated.UnitsTable.Row unitsId, double amount) {
            mEventIdRow = eventId;
            mMealIdRow = mealId;
            mUnitsIdRow = unitsId;
            mAmount = amount;
        }

        public void setUnitsId(com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey unitsId) {
            if (((mUnitsId == null)&&(unitsId == null))||((mUnitsId!= null)&&mUnitsId.equals(unitsId))) {
                return ;
            }
            mUnitsId = unitsId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey getUnitsId() {
            return mUnitsId;
        }

        public void setAmount(double amount) {
            if (mAmount == amount) {
                return ;
            }
            mAmount = amount;
            setIsModified(true);
        }

        public double getAmount() {
            return mAmount;
        }

        @Override
        public Object insert(Transaction transaction) {
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                mEventId = mEventIdRow.getNextPrimaryKey();
            }
            if (mMealIdRow!= null) {
                mMealIdRow.applyTo(transaction);
                mMealId = mMealIdRow.getNextPrimaryKey();
            }
            if (mUnitsIdRow!= null) {
                mUnitsIdRow.applyTo(transaction);
                mUnitsId = mUnitsIdRow.getNextPrimaryKey();
            }
            MealEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new MealEventTable.PrimaryKey(mEventIdRow.getNextPrimaryKey(), mMealIdRow.getNextPrimaryKey()));
                nextPrimaryKey = getNextPrimaryKey();
            }
            // This table does not use a row ID as a primary key.
            transaction.insert(COLUMN_NAMES, nextPrimaryKey.getEventId(), nextPrimaryKey.getMealId(), mUnitsId, mAmount);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(true);
                    setIsModified(false);
                    updatePrimaryKeyFromNext();
                }

            }
            );
            return nextPrimaryKey;
        }

        @Override
        public void update(Transaction transaction) {
            if (!this.isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES, mUnitsId, mAmount);
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed(1, actual);
            }
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsModified(false);
                }

            }
            );
        }

        @Override
        public void remove(Transaction transaction) {
            if (!this.isInDatabase()) {
                return ;
            }
            int actual = transaction.remove(getConcretePrimaryKey());
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.RemoveFailed(1, actual);
            }
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(false);
                    setIsDeleted(true);
                }

            }
            );
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MealEventTable.Row)) {
                return false;
            }
            MealEventTable.Row theRow = ((MealEventTable.Row) other);
            if (!(((mUnitsId == null)&&(theRow.mUnitsId == null))||((mUnitsId!= null)&&mUnitsId.equals(theRow.mUnitsId)))) {
                return false;
            }
            if (!(mAmount == theRow.mAmount)) {
                return false;
            }
            MealEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            MealEventTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            MealEventTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            MealEventTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
