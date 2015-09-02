
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.AndWhere;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
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
    public final static class HealthScoreEventTablePrimaryKey
        implements Key
    {

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

        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(event_id = ");
            where.append(mEventId.getId());
            where.append(")");
            where.append(" AND (health_score_id = ");
            where.append(mHealthScoreId.getId());
            where.append(")");
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row
        extends BaseRow<HealthScoreEventTable.HealthScoreEventTablePrimaryKey>
    {

        private long mScore;
        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey mHealthScoreId;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row mHealthScoreIdRow;

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("health_score_id");
            COLUMN_NAMES.add("score");
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row healthScoreTableRow, long score) {
            mEventIdRow = eventTableRow;
            mHealthScoreIdRow = healthScoreTableRow;
            mScore = score;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey healthScoreTablePrimaryKey, long score) {
            mEventId = eventTablePrimaryKey;
            mHealthScoreId = healthScoreTablePrimaryKey;
            mScore = score;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            if (((mEventId == null)&&(eventId == null))||((mEventId!= null)&&mEventId.equals(eventId))) {
                return ;
            }
            mEventId = eventId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setHealthScoreId(com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey healthScoreId) {
            if (((mHealthScoreId == null)&&(healthScoreId == null))||((mHealthScoreId!= null)&&mHealthScoreId.equals(healthScoreId))) {
                return ;
            }
            mHealthScoreId = healthScoreId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey getHealthScoreId() {
            return mHealthScoreId;
        }

        public void setScore(long score) {
            if (mScore == score) {
                return ;
            }
            mScore = score;
            setIsModified(true);
        }

        public long getScore() {
            return mScore;
        }

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] eventId = new com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] {mEventId };
            if (mEventId == null) {
                eventId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey) mEventIdRow.insert(transaction));
            }
            final com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey[] healthScoreId = new com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey[] {mHealthScoreId };
            if (mHealthScoreId == null) {
                healthScoreId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey) mHealthScoreIdRow.insert(transaction));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, eventId[ 0 ], healthScoreId[ 0 ], mScore);
            final HealthScoreEventTable.HealthScoreEventTablePrimaryKey primaryKey = new HealthScoreEventTable.HealthScoreEventTablePrimaryKey(eventId[ 0 ], healthScoreId[ 0 ]);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mEventId = eventId[ 0 ];
                    mHealthScoreId = healthScoreId[ 0 ];
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
            int actual = transaction.update(new AndWhere(mEventId, mHealthScoreId), COLUMN_NAMES, mEventId, mHealthScoreId, mScore);
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
            int actual = transaction.remove(new AndWhere(mEventId, mHealthScoreId));
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
            if (!(other instanceof HealthScoreEventTable.Row)) {
                return false;
            }
            HealthScoreEventTable.Row theRow = ((HealthScoreEventTable.Row) other);
            if (!(((mEventId == null)&&(theRow.mEventId == null))||((mEventId!= null)&&mEventId.equals(theRow.mEventId)))) {
                return false;
            }
            if (!(((mHealthScoreId == null)&&(theRow.mHealthScoreId == null))||((mHealthScoreId!= null)&&mHealthScoreId.equals(theRow.mHealthScoreId)))) {
                return false;
            }
            if (!(mScore == theRow.mScore)) {
                return false;
            }
            return true;
        }

    }

}
