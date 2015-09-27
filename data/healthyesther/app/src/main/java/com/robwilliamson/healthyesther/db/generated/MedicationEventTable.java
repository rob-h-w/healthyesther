
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
public final class MedicationEventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Key
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
    public final static class Row
        extends BaseRow<MedicationEventTable.PrimaryKey>
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey mMedicationId;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(0);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("medication_id");
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

        private void applyToRows(Transaction transaction) {
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                mEventId = mEventIdRow.getNextPrimaryKey();
            }
            if (mMedicationIdRow!= null) {
                mMedicationIdRow.applyTo(transaction);
                mMedicationId = mMedicationIdRow.getNextPrimaryKey();
            }
        }

        @Override
        protected Object insert(Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            // This table does not use a row ID as a primary key.
            setNextPrimaryKey(new MedicationEventTable.PrimaryKey(mEventId, mMedicationId));
            MedicationEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            transaction.insert(COLUMN_NAMES, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getMedicationId().getId());
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
            throw new UnsupportedOperationException();
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

    }

}
