
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class HealthScoreEventTable
    extends Table
    implements Serializable
{

    public final static String EVENT_ID = "event_id";
    public final static String HEALTH_SCORE_ID = "health_score_id";
    public final static String SCORE = "score";

    @Nonnull
    @Override
    public String getName() {
        return "health_score_event";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE health_score_event ( \n    health_score_id         NOT NULL\n                            REFERENCES health_score ( _id ) ON DELETE CASCADE\n                                                            ON UPDATE CASCADE,\n    event_id                NOT NULL\n                            REFERENCES event ( _id ) ON DELETE CASCADE\n                                                     ON UPDATE CASCADE,\n    score           INTEGER,\n    PRIMARY KEY ( health_score_id, event_id ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS health_score_event");
    }

    @Nonnull
    public HealthScoreEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        final HealthScoreEventTable.Row[] rows = new HealthScoreEventTable.Row[cursor.count()] ;
        int index = 0;
        if (cursor.count()> 0) {
            cursor.moveToFirst();
            do {
                rows[index ++] = new HealthScoreEventTable.Row(cursor);
            } while (cursor.moveToNext());
        }
        return rows;
    }

    @Nonnull
    public HealthScoreEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        HealthScoreEventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Serializable, Key
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey mHealthScoreId;

        public PrimaryKey(HealthScoreEventTable.PrimaryKey other) {
            mEventId = other.mEventId;
            mHealthScoreId = other.mHealthScoreId;
        }

        public PrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey healthScoreId) {
            mEventId = eventId;
            mHealthScoreId = healthScoreId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey getEventId() {
            return mEventId;
        }

        public void setHealthScoreId(com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey healthScoreId) {
            mHealthScoreId = healthScoreId;
        }

        public com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey getHealthScoreId() {
            return mHealthScoreId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof HealthScoreEventTable.PrimaryKey)) {
                return false;
            }
            HealthScoreEventTable.PrimaryKey thePrimaryKey = ((HealthScoreEventTable.PrimaryKey) other);
            if (!(((mEventId == null)&&(thePrimaryKey.mEventId == null))||((mEventId!= null)&&mEventId.equals(thePrimaryKey.mEventId)))) {
                return false;
            }
            if (!(((mHealthScoreId == null)&&(thePrimaryKey.mHealthScoreId == null))||((mHealthScoreId!= null)&&mHealthScoreId.equals(thePrimaryKey.mHealthScoreId)))) {
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
    public static class Row
        extends BaseRow<HealthScoreEventTable.PrimaryKey>
        implements Serializable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row mHealthScoreIdRow;
        private Long mScore;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(1);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("health_score_id");
            COLUMN_NAMES.add("score");
            COLUMN_NAMES_FOR_UPDATE.add("score");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setScore(cursor.getLong("score"));
            setPrimaryKey(new HealthScoreEventTable.PrimaryKey(((cursor.getLong("event_id")!= null)?new com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey(cursor.getLong("event_id")):null), ((cursor.getLong("health_score_id")!= null)?new com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey(cursor.getLong("health_score_id")):null)));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey healthScoreId, long score) {
            setPrimaryKey(new HealthScoreEventTable.PrimaryKey(eventId, healthScoreId));
            mScore = score;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row healthScoreId, long score) {
            mEventIdRow = eventId;
            mHealthScoreIdRow = healthScoreId;
            mScore = score;
        }

        public void setScore(Long score) {
            if (((mScore == null)&&(score == null))||((mScore!= null)&&mScore.equals(score))) {
                return ;
            }
            mScore = score;
            setIsModified(true);
        }

        public Long getScore() {
            return mScore;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
            }
            if (mHealthScoreIdRow!= null) {
                mHealthScoreIdRow.applyTo(transaction);
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            final Object score = ((mScore == null)?Long.class:mScore);
            // This table does not use a row ID as a primary key.
            setNextPrimaryKey(new HealthScoreEventTable.PrimaryKey(getConcretePrimaryKey().getEventId(), getConcretePrimaryKey().getHealthScoreId()));
            HealthScoreEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            transaction.insert("health_score_event", COLUMN_NAMES, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getHealthScoreId().getId(), score);
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
            final Object score = ((mScore == null)?Long.class:mScore);
            int actual = transaction.update("health_score_event", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, score);
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
            int actual = transaction.remove("health_score_event", getConcretePrimaryKey());
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
            if (!(other instanceof HealthScoreEventTable.Row)) {
                return false;
            }
            HealthScoreEventTable.Row theRow = ((HealthScoreEventTable.Row) other);
            if (!(((mScore == null)&&(theRow.mScore == null))||((mScore!= null)&&mScore.equals(theRow.mScore)))) {
                return false;
            }
            HealthScoreEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            HealthScoreEventTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            HealthScoreEventTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            HealthScoreEventTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
