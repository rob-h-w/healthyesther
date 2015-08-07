
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
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
    public final static class HealthScoreTablePrimaryKey {

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

        public Row(long bestValue, String name, boolean randomQuery, HealthScoreTable.HealthScoreTablePrimaryKey id, String maxLabel, String minLabel) {
        }

        public void setId(HealthScoreTable.HealthScoreTablePrimaryKey id) {
            mId = id;
        }

        public HealthScoreTable.HealthScoreTablePrimaryKey getId() {
            return mId;
        }

        public void setBestValue(long bestValue) {
            mBestValue = bestValue;
        }

        public long getBestValue() {
            return mBestValue;
        }

        public void setMaxLabel(String maxLabel) {
            mMaxLabel = maxLabel;
        }

        public String getMaxLabel() {
            return mMaxLabel;
        }

        public void setMinLabel(String minLabel) {
            mMinLabel = minLabel;
        }

        public String getMinLabel() {
            return mMinLabel;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setRandomQuery(boolean randomQuery) {
            mRandomQuery = randomQuery;
        }

        public boolean getRandomQuery() {
            return mRandomQuery;
        }

        @Override
        public void insert(Transaction transaction) {
        }

    }

}
