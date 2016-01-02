
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
        transaction.execSQL("CREATE TABLE IF NOT EXISTS health_score_event ( \n    health_score_id         NOT NULL\n                            REFERENCES health_score ( _id ) ON DELETE CASCADE\n                                                            ON UPDATE CASCADE,\n    event_id                NOT NULL\n                            REFERENCES event ( _id ) ON DELETE CASCADE\n                                                     ON UPDATE CASCADE,\n    score           INTEGER,\n    PRIMARY KEY ( health_score_id, event_id ) \n)");
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
        try {
            final HealthScoreEventTable.Row[] rows = new HealthScoreEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new HealthScoreEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public HealthScoreEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        HealthScoreEventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public HealthScoreEventTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        HealthScoreEventTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public HealthScoreEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final HealthScoreEventTable.Row[] rows = new HealthScoreEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new HealthScoreEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public HealthScoreEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        HealthScoreEventTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public HealthScoreEventTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        HealthScoreEventTable.Row row = select0Or1(database, where);
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

        @Nullable
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
        @Nullable
        private Long mScore;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(3);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES_FOR_UPDATE.add("event_id");
            COLUMN_NAMES.add("health_score_id");
            COLUMN_NAMES_FOR_UPDATE.add("health_score_id");
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
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.PrimaryKey healthScoreId,
            @Nullable
            Long score) {
            setPrimaryKey(new HealthScoreEventTable.PrimaryKey(eventId, healthScoreId));
            mScore = score;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row healthScoreId,
            @Nullable
            Long score) {
            setNextPrimaryKey(new HealthScoreEventTable.PrimaryKey(null, null));
            mEventIdRow = eventId;
            mHealthScoreIdRow = healthScoreId;
            mScore = score;
        }

        public void setScore(
            @Nullable
            Long score) {
            if (((mScore == null)&&(score == null))||((mScore!= null)&&mScore.equals(score))) {
                return ;
            }
            mScore = score;
            setIsModified(true);
        }

        @Nullable
        public Long getScore() {
            return mScore;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            HealthScoreEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setEventId(mEventIdRow.getNextPrimaryKey());
                }
            }
            if (mHealthScoreIdRow!= null) {
                mHealthScoreIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setHealthScoreId(mHealthScoreIdRow.getNextPrimaryKey());
                }
            }
            if (((nextPrimaryKey == null)&&(mEventIdRow!= null))&&(mHealthScoreIdRow!= null)) {
                HealthScoreEventTable.PrimaryKey oldNextPrimaryKey = getNextPrimaryKey();
                setNextPrimaryKey(new HealthScoreEventTable.PrimaryKey(mEventIdRow.getNextPrimaryKey(), mHealthScoreIdRow.getNextPrimaryKey()));
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
            HealthScoreEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new HealthScoreEventTable.PrimaryKey(getConcretePrimaryKey().getEventId(), getConcretePrimaryKey().getHealthScoreId()));
                nextPrimaryKey = getNextPrimaryKey();
            }
            // This table does not use a row ID as a primary key.
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
            HealthScoreEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("health_score_event", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getHealthScoreId().getId(), score);
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

        @Override
        public boolean isValid() {
            return true;
        }

    }

}
