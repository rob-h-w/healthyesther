
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
public class MedicationEventTable
    extends Table
    implements Serializable
{

    public final static String EVENT_ID = "event_id";
    public final static String MEDICATION_ID = "medication_id";

    @Nonnull
    @Override
    public String getName() {
        return "medication_event";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS medication_event ( \n    medication_id  NOT NULL\n                   REFERENCES medication ( _id ) ON DELETE CASCADE\n                                                 ON UPDATE CASCADE,\n    event_id       NOT NULL\n                   REFERENCES event ( _id ) ON DELETE CASCADE\n                                            ON UPDATE CASCADE,\n    PRIMARY KEY ( medication_id, event_id ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS medication_event");
    }

    @Nonnull
    public MedicationEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        try {
            final MedicationEventTable.Row[] rows = new MedicationEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new MedicationEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public MedicationEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MedicationEventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public MedicationEventTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        MedicationEventTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public MedicationEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final MedicationEventTable.Row[] rows = new MedicationEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new MedicationEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public MedicationEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MedicationEventTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public MedicationEventTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        MedicationEventTable.Row row = select0Or1(database, where);
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
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey mMedicationId;

        public PrimaryKey(MedicationEventTable.PrimaryKey other) {
            mEventId = other.mEventId;
            mMedicationId = other.mMedicationId;
        }

        public PrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            mEventId = eventId;
            mMedicationId = medicationId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey getEventId() {
            return mEventId;
        }

        public void setMedicationId(com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            mMedicationId = medicationId;
        }

        public com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey getMedicationId() {
            return mMedicationId;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationEventTable.PrimaryKey)) {
                return false;
            }
            MedicationEventTable.PrimaryKey thePrimaryKey = ((MedicationEventTable.PrimaryKey) other);
            if (!(((mEventId == null)&&(thePrimaryKey.mEventId == null))||((mEventId!= null)&&mEventId.equals(thePrimaryKey.mEventId)))) {
                return false;
            }
            if (!(((mMedicationId == null)&&(thePrimaryKey.mMedicationId == null))||((mMedicationId!= null)&&mMedicationId.equals(thePrimaryKey.mMedicationId)))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = MedicationEventTable.PrimaryKey.class.getCanonicalName().hashCode();
            hash = (hash^mEventId.hashCode());
            hash = (hash^mMedicationId.hashCode());
            return hash;
        }

        @Nullable
        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(event_id = ");
            where.append(mEventId.getId());
            where.append(")");
            where.append(" AND (medication_id = ");
            where.append(mMedicationId.getId());
            where.append(")");
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class Row
        extends BaseRow<MedicationEventTable.PrimaryKey>
        implements Serializable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(2);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES_FOR_UPDATE.add("event_id");
            COLUMN_NAMES.add("medication_id");
            COLUMN_NAMES_FOR_UPDATE.add("medication_id");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setPrimaryKey(new MedicationEventTable.PrimaryKey(((cursor.getLong("event_id")!= null)?new com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey(cursor.getLong("event_id")):null), ((cursor.getLong("medication_id")!= null)?new com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey(cursor.getLong("medication_id")):null)));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            setPrimaryKey(new MedicationEventTable.PrimaryKey(eventId, medicationId));
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationId) {
            mEventIdRow = eventId;
            mMedicationIdRow = medicationId;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            MedicationEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setEventId(mEventIdRow.getNextPrimaryKey());
                }
            }
            if (mMedicationIdRow!= null) {
                mMedicationIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setMedicationId(mMedicationIdRow.getNextPrimaryKey());
                }
            }
            if (((nextPrimaryKey == null)&&(mEventIdRow!= null))&&(mMedicationIdRow!= null)) {
                MedicationEventTable.PrimaryKey oldNextPrimaryKey = getNextPrimaryKey();
                setNextPrimaryKey(new MedicationEventTable.PrimaryKey(mEventIdRow.getNextPrimaryKey(), mMedicationIdRow.getNextPrimaryKey()));
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            MedicationEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new MedicationEventTable.PrimaryKey(getConcretePrimaryKey().getEventId(), getConcretePrimaryKey().getMedicationId()));
                nextPrimaryKey = getNextPrimaryKey();
            }
            // This table does not use a row ID as a primary key.
            transaction.insert("medication_event", COLUMN_NAMES, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getMedicationId().getId());
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
            MedicationEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("medication_event", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getMedicationId().getId());
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
            int actual = transaction.remove("medication_event", getConcretePrimaryKey());
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

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationEventTable.Row)) {
                return false;
            }
            MedicationEventTable.Row theRow = ((MedicationEventTable.Row) other);
            MedicationEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            MedicationEventTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            MedicationEventTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            MedicationEventTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public void loadRelations(Database database) {
            com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId = getConcretePrimaryKey().getEventId();
            if (mEventIdRow == null) {
                mEventIdRow = HealthDatabase.EVENT_TABLE.select1(database, eventId);
                mEventIdRow.loadRelations(database);
            }
            com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId = getConcretePrimaryKey().getMedicationId();
            if (mMedicationIdRow == null) {
                mMedicationIdRow = HealthDatabase.MEDICATION_TABLE.select1(database, medicationId);
            }
        }

        @Nonnull
        public com.robwilliamson.healthyesther.db.generated.EventTable.Row getEventRow() {
            if (mEventIdRow == null) {
                throw new NullPointerException("event row is not set - call loadRelations first.");
            }
            return mEventIdRow;
        }

        @Nonnull
        public com.robwilliamson.healthyesther.db.generated.MedicationTable.Row getMedicationRow() {
            if (mMedicationIdRow == null) {
                throw new NullPointerException("medication row is not set - call loadRelations first.");
            }
            return mMedicationIdRow;
        }

        @Override
        public int hashCode() {
            int hash = MedicationEventTable.Row.class.getCanonicalName().hashCode();
            MedicationEventTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            if (primaryKey!= null) {
                hash = ((primaryKey.getEventId() == null)?hash:(hash^primaryKey.getEventId().hashCode()));
                hash = ((primaryKey.getMedicationId() == null)?hash:(hash^primaryKey.getMedicationId().hashCode()));
            }
            return hash;
        }

    }

}
