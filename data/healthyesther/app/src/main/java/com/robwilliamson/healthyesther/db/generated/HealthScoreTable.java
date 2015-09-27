
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
public final class HealthScoreTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Key
    {

        private long mId;

        public PrimaryKey(HealthScoreTable.PrimaryKey other) {
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
            if (!(other instanceof HealthScoreTable.PrimaryKey)) {
                return false;
            }
            HealthScoreTable.PrimaryKey thePrimaryKey = ((HealthScoreTable.PrimaryKey) other);
            if (!(mId == thePrimaryKey.mId)) {
                return false;
            }
            return true;
        }

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
    public final static class Row
        extends BaseRow<HealthScoreTable.PrimaryKey>
    {

        @Nonnull
        private long mBestValue;
        @Nonnull
        private String mName;
        @Nonnull
        private boolean mRandomQuery;
        private String mMaxLabel;
        private String mMinLabel;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(6);
        public final static ArrayList<String> INSERT_LIST = new ArrayList<String>(5);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("best_value");
            INSERT_LIST.add("best_value");
            COLUMN_NAMES.add("name");
            INSERT_LIST.add("name");
            COLUMN_NAMES.add("random_query");
            INSERT_LIST.add("random_query");
            COLUMN_NAMES.add("max_label");
            INSERT_LIST.add("max_label");
            COLUMN_NAMES.add("min_label");
            INSERT_LIST.add("min_label");
        }

        public Row(
            @Nonnull
            long bestValue,
            @Nonnull
            String name,
            @Nonnull
            boolean randomQuery, String maxLabel, String minLabel) {
            mBestValue = bestValue;
            mName = name;
            mRandomQuery = randomQuery;
            mMaxLabel = maxLabel;
            mMinLabel = minLabel;
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

        public void setName(String name) {
            if (((mName == null)&&(name == null))||((mName!= null)&&mName.equals(name))) {
                return ;
            }
            mName = name;
            setIsModified(true);
        }

        public String getName() {
            return mName;
        }

        public void setRandomQuery(boolean randomQuery) {
            if (mRandomQuery == randomQuery) {
                return ;
            }
            mRandomQuery = randomQuery;
            setIsModified(true);
        }

        public boolean getRandomQuery() {
            return mRandomQuery;
        }

        public void setMaxLabel(String maxLabel) {
            if (((mMaxLabel == null)&&(maxLabel == null))||((mMaxLabel!= null)&&mMaxLabel.equals(maxLabel))) {
                return ;
            }
            mMaxLabel = maxLabel;
            setIsModified(true);
        }

        public String getMaxLabel() {
            return mMaxLabel;
        }

        public void setMinLabel(String minLabel) {
            if (((mMinLabel == null)&&(minLabel == null))||((mMinLabel!= null)&&mMinLabel.equals(minLabel))) {
                return ;
            }
            mMinLabel = minLabel;
            setIsModified(true);
        }

        public String getMinLabel() {
            return mMinLabel;
        }

        @Override
        protected Object insert(Transaction transaction) {
            HealthScoreTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new HealthScoreTable.PrimaryKey(transaction.insert(INSERT_LIST, mBestValue, mName, mRandomQuery, mMaxLabel, mMinLabel)));
            }
            // This table uses a row ID as a primary key.
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
        protected void update(Transaction transaction) {
            if (!this.isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES, mBestValue, mName, mRandomQuery, mMaxLabel, mMinLabel);
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
        protected void remove(Transaction transaction) {
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
            if (!(other instanceof HealthScoreTable.Row)) {
                return false;
            }
            HealthScoreTable.Row theRow = ((HealthScoreTable.Row) other);
            if (!(mBestValue == theRow.mBestValue)) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(mRandomQuery == theRow.mRandomQuery)) {
                return false;
            }
            if (!(((mMaxLabel == null)&&(theRow.mMaxLabel == null))||((mMaxLabel!= null)&&mMaxLabel.equals(theRow.mMaxLabel)))) {
                return false;
            }
            if (!(((mMinLabel == null)&&(theRow.mMinLabel == null))||((mMinLabel!= null)&&mMinLabel.equals(theRow.mMinLabel)))) {
                return false;
            }
            HealthScoreTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            HealthScoreTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            HealthScoreTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            HealthScoreTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
