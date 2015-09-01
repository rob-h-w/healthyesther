
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


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
    public final static class HealthScoreTablePrimaryKey
        implements Where
    {

        private long mId;

        public HealthScoreTablePrimaryKey(HealthScoreTable.HealthScoreTablePrimaryKey other) {
            mId = other.mId;
        }

        public HealthScoreTablePrimaryKey(long id) {
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
            if (!(other instanceof HealthScoreTable.HealthScoreTablePrimaryKey)) {
                return false;
            }
            HealthScoreTable.HealthScoreTablePrimaryKey theHealthScoreTablePrimaryKey = ((HealthScoreTable.HealthScoreTablePrimaryKey) other);
            if (theHealthScoreTablePrimaryKey.mId!= mId) {
                return false;
            }
            return true;
        }

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
        extends BaseTransactable
    {

        private String mMaxLabel;
        private String mMinLabel;
        private long mBestValue;
        private String mName;
        private boolean mRandomQuery;
        private HealthScoreTable.HealthScoreTablePrimaryKey mId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(6);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("best_value");
            COLUMN_NAMES.add("max_label");
            COLUMN_NAMES.add("min_label");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("random_query");
        }

        public Row(
            @Nonnull
            long bestValue,
            @Nonnull
            String name,
            @Nonnull
            boolean randomQuery, HealthScoreTable.HealthScoreTablePrimaryKey id, String maxLabel, String minLabel) {
            mBestValue = bestValue;
            mName = name;
            mRandomQuery = randomQuery;
            mId = id;
            mMaxLabel = maxLabel;
            mMinLabel = minLabel;
        }

        public void setId(HealthScoreTable.HealthScoreTablePrimaryKey id) {
            if (((mId == null)&&(id == null))||((mId!= null)&&mId.equals(id))) {
                return ;
            }
            mId = id;
            setIsModified(true);
        }

        public HealthScoreTable.HealthScoreTablePrimaryKey getId() {
            return mId;
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

        @Override
        public Object insert(Transaction transaction) {
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mBestValue, mMaxLabel, mMinLabel, mName, mRandomQuery);
            final HealthScoreTable.HealthScoreTablePrimaryKey primaryKey = new HealthScoreTable.HealthScoreTablePrimaryKey(rowId);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mId = primaryKey;
                    setIsInDatabase(true);
                    setIsModified(false);
                }

            }
            );
            return primaryKey;
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
            if (!(((mId == null)&&(theRow.mId == null))||((mId!= null)&&mId.equals(theRow.mId)))) {
                return false;
            }
            if (!(mBestValue == theRow.mBestValue)) {
                return false;
            }
            if (!(((mMaxLabel == null)&&(theRow.mMaxLabel == null))||((mMaxLabel!= null)&&mMaxLabel.equals(theRow.mMaxLabel)))) {
                return false;
            }
            if (!(((mMinLabel == null)&&(theRow.mMinLabel == null))||((mMinLabel!= null)&&mMinLabel.equals(theRow.mMinLabel)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(mRandomQuery == theRow.mRandomQuery)) {
                return false;
            }
            return true;
        }

    }

}
