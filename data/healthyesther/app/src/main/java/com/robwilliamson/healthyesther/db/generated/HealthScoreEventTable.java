
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class HealthScoreEventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class HealthScoreEventTablePrimaryKey {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey mHealthScoreId;

        public HealthScoreEventTablePrimaryKey(HealthScoreEventTable.HealthScoreEventTablePrimaryKey other) {
            mEventId = other.mEventId;
            mHealthScoreId = other.mHealthScoreId;
        }

        public HealthScoreEventTablePrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey healthScoreId) {
            mEventId = eventId;
            mHealthScoreId = healthScoreId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setHealthScoreId(com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey healthScoreId) {
            mHealthScoreId = healthScoreId;
        }

        public com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey getHealthScoreId() {
            return mHealthScoreId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof HealthScoreEventTable.HealthScoreEventTablePrimaryKey)) {
                return false;
            }
            HealthScoreEventTable.HealthScoreEventTablePrimaryKey theHealthScoreEventTablePrimaryKey = ((HealthScoreEventTable.HealthScoreEventTablePrimaryKey) other);
            if (theHealthScoreEventTablePrimaryKey.mEventId!= mEventId) {
                if ((theHealthScoreEventTablePrimaryKey.mEventId == null)||(!theHealthScoreEventTablePrimaryKey.mEventId.equals(mEventId))) {
                    return false;
                }
            }
            if (theHealthScoreEventTablePrimaryKey.mHealthScoreId!= mHealthScoreId) {
                if ((theHealthScoreEventTablePrimaryKey.mHealthScoreId == null)||(!theHealthScoreEventTablePrimaryKey.mHealthScoreId.equals(mHealthScoreId))) {
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

        private long mScore;
        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey mHealthScoreId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(3);
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row mHealthScoreIdRow;

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("health_score_id");
            COLUMN_NAMES.add("score");
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row healthScoreTableRow, Long score, com.robwilliamson.healthyesther.db.generated.EventTable.Row rowEventId, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row rowHealthScoreId) {
            mEventIdRow = rowEventId;
            mHealthScoreIdRow = rowHealthScoreId;
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey healthScoreTablePrimaryKey, Long score) {
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setHealthScoreId(com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey healthScoreId) {
            mHealthScoreId = healthScoreId;
        }

        public com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey getHealthScoreId() {
            return mHealthScoreId;
        }

        public void setScore(long score) {
            mScore = score;
        }

        public long getScore() {
            return mScore;
        }

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] eventId = new com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] {mEventId };
            if (mEventId!= null) {
                mEventId = mEventIdRow.insert(transaction);
            }
            final com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey[] healthScoreId = new com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey[] {mHealthScoreId };
            if (mHealthScoreId!= null) {
                mHealthScoreId = mHealthScoreIdRow.insert(transaction);
            }
            final HealthScoreEventTable.HealthScoreEventTablePrimaryKey primaryKey = new HealthScoreEventTable.HealthScoreEventTablePrimaryKey(transaction.insert(COLUMN_NAMES, mScore));
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mEventId = primaryKey;
                }

            }
            );
            return primaryKey;
        }

    }

}
