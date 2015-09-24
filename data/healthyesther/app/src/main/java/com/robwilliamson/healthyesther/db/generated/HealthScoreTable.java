
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
            if (thePrimaryKey.mId!= mId) {
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

        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(6);
        private HealthScoreTable.PrimaryKey mId;
        @Nonnull
        private long mBestValue;
        @Nonnull
        private String mName;
        @Nonnull
        private boolean mRandomQuery;
        private String mMaxLabel;
        private String mMinLabel;

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("best_value");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("random_query");
            COLUMN_NAMES.add("max_label");
            COLUMN_NAMES.add("min_label");
        }

        public Row(
            @Nonnull
            long bestValue,
            @Nonnull
            String name,
            @Nonnull
            boolean randomQuery, String maxLabel, String minLabel) {
            setPrimaryKey(new HealthScoreTable.PrimaryKey());
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
        public Object insert(Transaction transaction) {
            getConcretePrimaryKey();
            HealthScoreTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            boolean constructPrimaryKey = (!(primaryKey == null));
            if (constructPrimaryKey) {
                setPrimaryKey(new HealthScoreTable.PrimaryKey(primaryKey.getId(), rowId));
                primaryKey = setPrimaryKey(new HealthScoreTable.PrimaryKey(primaryKey.getId(), rowId));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, primaryKey.getId(), mBestValue, mName, mRandomQuery, mMaxLabel, mMinLabel);
            final HealthScoreTable.PrimaryKey primaryKey = primaryKey;
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    primaryKey.setId(rowId);
                    setIsInDatabase(true);
                    setIsModified(false);
                }

            }
            );
            return primaryKey;
        }

        @Override
        public void update(Transaction transaction) {
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
            return true;
        }

    }

}
