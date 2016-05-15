
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
public class HealthScoreJudgmentRangeTable
    extends Table
    implements Serializable
{

    public final static String _ID = "_id";
    public final static String SCORE_ID = "score_id";
    public final static String BEST_VALUE = "best_value";
    public final static String END_TIME = "end_time";
    public final static String START_TIME = "start_time";

    @Nonnull
    @Override
    public String getName() {
        return "health_score_judgment_range";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS health_score_judgment_range (_id INTEGER PRIMARY KEY NOT NULL UNIQUE, score_id REFERENCES health_score (_id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, best_value INTEGER NOT NULL, start_time INTEGER, end_time INTEGER)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS health_score_judgment_range");
    }

    @Nonnull
    public HealthScoreJudgmentRangeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        try {
            final HealthScoreJudgmentRangeTable.Row[] rows = new HealthScoreJudgmentRangeTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new HealthScoreJudgmentRangeTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public HealthScoreJudgmentRangeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        HealthScoreJudgmentRangeTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public HealthScoreJudgmentRangeTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        HealthScoreJudgmentRangeTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public HealthScoreJudgmentRangeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final HealthScoreJudgmentRangeTable.Row[] rows = new HealthScoreJudgmentRangeTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new HealthScoreJudgmentRangeTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public HealthScoreJudgmentRangeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        HealthScoreJudgmentRangeTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public HealthScoreJudgmentRangeTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        HealthScoreJudgmentRangeTable.Row row = select0Or1(database, where);
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

        private long mId;

        public PrimaryKey(HealthScoreJudgmentRangeTable.PrimaryKey other) {
            mId = other.mId;
        }

        public PrimaryKey(long id) {
            mId = id;
        }

        public void setId(long id) {
            mId = id;
        }

        public long getId() {
            return mId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof HealthScoreJudgmentRangeTable.PrimaryKey)) {
                return false;
            }
            HealthScoreJudgmentRangeTable.PrimaryKey thePrimaryKey = ((HealthScoreJudgmentRangeTable.PrimaryKey) other);
            if (!(mId == thePrimaryKey.mId)) {
                return false;
            }
            return true;
        }

        @Nullable
        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(_id = ");
            where.append(mId);
            where.append(")");
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class Row
        extends BaseRow<HealthScoreJudgmentRangeTable.PrimaryKey>
        implements Serializable
    {

        @Nullable
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey mScoreId;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row mScoreIdRow;
        private long mBestValue;
        @Nullable
        private Long mEndTime;
        @Nullable
        private Long mStartTime;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(5);
        public final static ArrayList<String> COLUMN_NAMES_FOR_INSERTION = new ArrayList<String>(4);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(5);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES_FOR_UPDATE.add("_id");
            COLUMN_NAMES.add("score_id");
            COLUMN_NAMES_FOR_INSERTION.add("score_id");
            COLUMN_NAMES_FOR_UPDATE.add("score_id");
            COLUMN_NAMES.add("best_value");
            COLUMN_NAMES_FOR_INSERTION.add("best_value");
            COLUMN_NAMES_FOR_UPDATE.add("best_value");
            COLUMN_NAMES.add("end_time");
            COLUMN_NAMES_FOR_INSERTION.add("end_time");
            COLUMN_NAMES_FOR_UPDATE.add("end_time");
            COLUMN_NAMES.add("start_time");
            COLUMN_NAMES_FOR_INSERTION.add("start_time");
            COLUMN_NAMES_FOR_UPDATE.add("start_time");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setScoreId(((cursor.getLong("score_id")!= null)?new com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey(cursor.getLong("score_id")):null));
            setBestValue(cursor.getLong("best_value"));
            setEndTime(cursor.getLong("end_time"));
            setStartTime(cursor.getLong("start_time"));
            setPrimaryKey(new HealthScoreJudgmentRangeTable.PrimaryKey(cursor.getLong("_id")));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey scoreId, long bestValue,
            @Nullable
            Long endTime,
            @Nullable
            Long startTime) {
            mScoreId = scoreId;
            mBestValue = bestValue;
            mEndTime = endTime;
            mStartTime = startTime;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row scoreId, long bestValue,
            @Nullable
            Long endTime,
            @Nullable
            Long startTime) {
            mScoreIdRow = scoreId;
            mBestValue = bestValue;
            mEndTime = endTime;
            mStartTime = startTime;
        }

        public void setScoreId(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey scoreId) {
            if (((mScoreId == null)&&(scoreId == null))||((mScoreId!= null)&&mScoreId.equals(scoreId))) {
                return ;
            }
            mScoreId = scoreId;
            setIsModified(true);
        }

        @Nonnull
        public com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey getScoreId() {
            return mScoreId;
        }

        public void setBestValue(long bestValue) {
            if (mBestValue == bestValue) {
                return ;
            }
            mBestValue = bestValue;
            setIsModified(true);
        }

        public long getBestValue() {
            return mBestValue;
        }

        public void setEndTime(
            @Nullable
            Long endTime) {
            if (((mEndTime == null)&&(endTime == null))||((mEndTime!= null)&&mEndTime.equals(endTime))) {
                return ;
            }
            mEndTime = endTime;
            setIsModified(true);
        }

        @Nullable
        public Long getEndTime() {
            return mEndTime;
        }

        public void setStartTime(
            @Nullable
            Long startTime) {
            if (((mStartTime == null)&&(startTime == null))||((mStartTime!= null)&&mStartTime.equals(startTime))) {
                return ;
            }
            mStartTime = startTime;
            setIsModified(true);
        }

        @Nullable
        public Long getStartTime() {
            return mStartTime;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            if (mScoreIdRow!= null) {
                mScoreIdRow.applyTo(transaction);
                mScoreId = mScoreIdRow.getNextPrimaryKey();
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            final Object endTime = ((mEndTime == null)?Long.class:mEndTime);
            final Object startTime = ((mStartTime == null)?Long.class:mStartTime);
            HealthScoreJudgmentRangeTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new HealthScoreJudgmentRangeTable.PrimaryKey(transaction.insert("health_score_judgment_range", COLUMN_NAMES_FOR_INSERTION, mScoreId.getId(), mBestValue, endTime, startTime)));
                nextPrimaryKey = getNextPrimaryKey();
            } else {
                nextPrimaryKey.setId(transaction.insert("health_score_judgment_range", COLUMN_NAMES_FOR_INSERTION, mScoreId.getId(), mBestValue, endTime, startTime));
            }
            // This table uses a row ID as a primary key.
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
            final Object endTime = ((mEndTime == null)?Long.class:mEndTime);
            final Object startTime = ((mStartTime == null)?Long.class:mStartTime);
            HealthScoreJudgmentRangeTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("health_score_judgment_range", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getId(), mScoreId.getId(), mBestValue, endTime, startTime);
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
            int actual = transaction.remove("health_score_judgment_range", getConcretePrimaryKey());
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
            if (!(other instanceof HealthScoreJudgmentRangeTable.Row)) {
                return false;
            }
            HealthScoreJudgmentRangeTable.Row theRow = ((HealthScoreJudgmentRangeTable.Row) other);
            if (!(((mScoreId == null)&&(theRow.mScoreId == null))||((mScoreId!= null)&&mScoreId.equals(theRow.mScoreId)))) {
                return false;
            }
            if (!(mBestValue == theRow.mBestValue)) {
                return false;
            }
            if (!(((mEndTime == null)&&(theRow.mEndTime == null))||((mEndTime!= null)&&mEndTime.equals(theRow.mEndTime)))) {
                return false;
            }
            if (!(((mStartTime == null)&&(theRow.mStartTime == null))||((mStartTime!= null)&&mStartTime.equals(theRow.mStartTime)))) {
                return false;
            }
            HealthScoreJudgmentRangeTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            HealthScoreJudgmentRangeTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            HealthScoreJudgmentRangeTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            HealthScoreJudgmentRangeTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
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
            if (mScoreIdRow == null) {
                mScoreIdRow = HealthDatabase.HEALTH_SCORE_TABLE.select1(database, mScoreId);
            }
        }

        @Nonnull
        public final com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row getHealthScoreRow() {
            if (mScoreIdRow == null) {
                throw new NullPointerException("health_score row is not set - call loadRelations first.");
            }
            return mScoreIdRow;
        }

    }

}
