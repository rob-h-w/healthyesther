
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.AndWhere;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


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
    public final static class MedicationEventTablePrimaryKey
        implements Where
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey mMedicationId;

        public MedicationEventTablePrimaryKey(MedicationEventTable.MedicationEventTablePrimaryKey other) {
            mEventId = other.mEventId;
            mMedicationId = other.mMedicationId;
        }

        public MedicationEventTablePrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationId) {
            mEventId = eventId;
            mMedicationId = medicationId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setMedicationId(com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationId) {
            mMedicationId = medicationId;
        }

        public com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey getMedicationId() {
            return mMedicationId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationEventTable.MedicationEventTablePrimaryKey)) {
                return false;
            }
            MedicationEventTable.MedicationEventTablePrimaryKey theMedicationEventTablePrimaryKey = ((MedicationEventTable.MedicationEventTablePrimaryKey) other);
            if (theMedicationEventTablePrimaryKey.mEventId!= mEventId) {
                if ((theMedicationEventTablePrimaryKey.mEventId == null)||(!theMedicationEventTablePrimaryKey.mEventId.equals(mEventId))) {
                    return false;
                }
            }
            if (theMedicationEventTablePrimaryKey.mMedicationId!= mMedicationId) {
                if ((theMedicationEventTablePrimaryKey.mMedicationId == null)||(!theMedicationEventTablePrimaryKey.mMedicationId.equals(mMedicationId))) {
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
        extends BaseTransactable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey mMedicationId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(2);
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("medication_id");
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow) {
            mEventIdRow = eventTableRow;
            mMedicationIdRow = medicationTableRow;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
            mEventId = eventTablePrimaryKey;
            mMedicationId = medicationTablePrimaryKey;
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

        public void setMedicationId(com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationId) {
            if (((mMedicationId == null)&&(medicationId == null))||((mMedicationId!= null)&&mMedicationId.equals(medicationId))) {
                return ;
            }
            mMedicationId = medicationId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey getMedicationId() {
            return mMedicationId;
        }

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] eventId = new com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] {mEventId };
            if (mEventId == null) {
                eventId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey) mEventIdRow.insert(transaction));
            }
            final com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] medicationId = new com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] {mMedicationId };
            if (mMedicationId == null) {
                medicationId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey) mMedicationIdRow.insert(transaction));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, eventId[ 0 ], medicationId[ 0 ]);
            final MedicationEventTable.MedicationEventTablePrimaryKey primaryKey = new MedicationEventTable.MedicationEventTablePrimaryKey(eventId[ 0 ], medicationId[ 0 ]);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mEventId = eventId[ 0 ];
                    mMedicationId = medicationId[ 0 ];
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
                throw new BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(new AndWhere(mEventId, mMedicationId), COLUMN_NAMES, mEventId, mMedicationId);
            if (actual!= 1) {
                throw new BaseTransactable.UpdateFailed(1, actual);
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
            int actual = transaction.remove(new AndWhere(mEventId, mMedicationId));
            if (actual!= 1) {
                throw new BaseTransactable.RemoveFailed(1, actual);
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
            if (!(((mEventId == null)&&(theRow.mEventId == null))||((mEventId!= null)&&mEventId.equals(theRow.mEventId)))) {
                return false;
            }
            if (!(((mMedicationId == null)&&(theRow.mMedicationId == null))||((mMedicationId!= null)&&mMedicationId.equals(theRow.mMedicationId)))) {
                return false;
            }
            return true;
        }

    }

}
