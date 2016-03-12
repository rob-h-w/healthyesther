
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Order;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class MealEventTable
    extends Table
    implements Serializable
{

    public final static String EVENT_ID = "event_id";
    public final static String MEAL_ID = "meal_id";
    public final static String UNITS_ID = "units_id";
    public final static String AMOUNT = "amount";

    @Nonnull
    @Override
    public String getName() {
        return "meal_event";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS meal_event ( \n    meal_id       NOT NULL\n                  REFERENCES meal ( _id ) ON DELETE CASCADE\n                                          ON UPDATE CASCADE,\n    event_id      NOT NULL\n                  REFERENCES event ( _id ) ON DELETE CASCADE\n                                           ON UPDATE CASCADE,\n    amount   REAL,\n    units_id      REFERENCES units ( _id ) ON DELETE SET NULL\n                                           ON UPDATE CASCADE,\n    PRIMARY KEY ( meal_id, event_id ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS meal_event");
    }

    @Nonnull
    public MealEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        try {
            final MealEventTable.Row[] rows = new MealEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new MealEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public MealEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MealEventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public MealEventTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        MealEventTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public MealEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final MealEventTable.Row[] rows = new MealEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new MealEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public MealEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MealEventTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public MealEventTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        MealEventTable.Row row = select0Or1(database, where);
        if (row == null) {
            throw new Table.NotFoundException(this, where);
        }
        return row;
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class PrimaryKey
        implements Serializable, Key
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

        @Nullable
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
    public static class Row
        extends BaseRow<MealEventTable.PrimaryKey>
        implements Serializable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.MealTable.Row mMealIdRow;
        @Nullable
        private com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey mUnitsId;
        private com.robwilliamson.healthyesther.db.generated.UnitsTable.Row mUnitsIdRow;
        @Nullable
        private Double mAmount;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(4);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(4);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES_FOR_UPDATE.add("event_id");
            COLUMN_NAMES.add("meal_id");
            COLUMN_NAMES_FOR_UPDATE.add("meal_id");
            COLUMN_NAMES.add("units_id");
            COLUMN_NAMES_FOR_UPDATE.add("units_id");
            COLUMN_NAMES.add("amount");
            COLUMN_NAMES_FOR_UPDATE.add("amount");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setUnitsId(((cursor.getLong("units_id")!= null)?new com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey(cursor.getLong("units_id")):null));
            setAmount(cursor.getDouble("amount"));
            setPrimaryKey(new MealEventTable.PrimaryKey(((cursor.getLong("event_id")!= null)?new com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey(cursor.getLong("event_id")):null), ((cursor.getLong("meal_id")!= null)?new com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey(cursor.getLong("meal_id")):null)));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MealTable.PrimaryKey mealId,
            @Nullable
            com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey unitsId,
            @Nullable
            Double amount) {
            setPrimaryKey(new MealEventTable.PrimaryKey(eventId, mealId));
            mUnitsId = unitsId;
            mAmount = amount;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MealTable.Row mealId,
            @Nullable
            com.robwilliamson.healthyesther.db.generated.UnitsTable.Row unitsId,
            @Nullable
            Double amount) {
            mEventIdRow = eventId;
            mMealIdRow = mealId;
            mUnitsIdRow = unitsId;
            mAmount = amount;
        }

        public void setUnitsId(
            @Nullable
            com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey unitsId) {
            if (((mUnitsId == null)&&(unitsId == null))||((mUnitsId!= null)&&mUnitsId.equals(unitsId))) {
                return ;
            }
            mUnitsId = unitsId;
            setIsModified(true);
        }

        @Nullable
        public com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey getUnitsId() {
            return mUnitsId;
        }

        public void setAmount(
            @Nullable
            Double amount) {
            if (((mAmount == null)&&(amount == null))||((mAmount!= null)&&mAmount.equals(amount))) {
                return ;
            }
            mAmount = amount;
            setIsModified(true);
        }

        @Nullable
        public Double getAmount() {
            return mAmount;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            MealEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setEventId(mEventIdRow.getNextPrimaryKey());
                }
            }
            if (mMealIdRow!= null) {
                mMealIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setMealId(mMealIdRow.getNextPrimaryKey());
                }
            }
            if (mUnitsIdRow!= null) {
                mUnitsIdRow.applyTo(transaction);
                mUnitsId = mUnitsIdRow.getNextPrimaryKey();
            }
            if (((nextPrimaryKey == null)&&(mEventIdRow!= null))&&(mMealIdRow!= null)) {
                MealEventTable.PrimaryKey oldNextPrimaryKey = getNextPrimaryKey();
                setNextPrimaryKey(new MealEventTable.PrimaryKey(mEventIdRow.getNextPrimaryKey(), mMealIdRow.getNextPrimaryKey()));
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            final Object unitsId = ((mUnitsId == null)?com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey.class:mUnitsId.getId());
            final Object amount = ((mAmount == null)?Double.class:mAmount);
            MealEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new MealEventTable.PrimaryKey(getConcretePrimaryKey().getEventId(), getConcretePrimaryKey().getMealId()));
                nextPrimaryKey = getNextPrimaryKey();
            }
            // This table does not use a row ID as a primary key.
            transaction.insert("meal_event", COLUMN_NAMES, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getMealId().getId(), unitsId, amount);
            setIsModified(false);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    updatePrimaryKeyFromNext();
                }

            }
            );
            setIsInDatabase(true);
            return nextPrimaryKey;
        }

        @Override
        protected void update(
            @Nonnull
            Transaction transaction) {
            if (!isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            applyToRows(transaction);
            final Object unitsId = ((mUnitsId == null)?com.robwilliamson.healthyesther.db.generated.UnitsTable.PrimaryKey.class:mUnitsId.getId());
            final Object amount = ((mAmount == null)?Double.class:mAmount);
            MealEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("meal_event", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getMealId().getId(), unitsId, amount);
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed(1, actual);
            }
            setIsModified(false);
        }

        @Override
        protected void remove(
            @Nonnull
            Transaction transaction) {
            if ((!isInDatabase())||isDeleted()) {
                return ;
            }
            int actual = transaction.remove("meal_event", getConcretePrimaryKey());
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.RemoveFailed(1, actual);
            }
            setIsDeleted(true);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(false);
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
            if (!(((mAmount == null)&&(theRow.mAmount == null))||((mAmount!= null)&&mAmount.equals(theRow.mAmount)))) {
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

        @Override
        public boolean isValid() {
            return true;
        }

        public final void loadRelations(Database database) {
            mEventIdRow = HealthDatabase.EVENT_TABLE.select1(database, getConcretePrimaryKey().getEventId());
            mMealIdRow = HealthDatabase.MEAL_TABLE.select1(database, getConcretePrimaryKey().getMealId());
            mUnitsIdRow = HealthDatabase.UNITS_TABLE.select0Or1(database, mUnitsId);
        }

    }

}
