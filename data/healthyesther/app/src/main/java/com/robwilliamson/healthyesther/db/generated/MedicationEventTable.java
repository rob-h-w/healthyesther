
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
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
    public final static class MedicationEventTablePrimaryKey {

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

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow, com.robwilliamson.healthyesther.db.generated.EventTable.Row rowEventId, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row rowMedicationId) {
            mEventIdRow = rowEventId;
            mMedicationIdRow = rowMedicationId;
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
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

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] eventId = new com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] {mEventId };
            if (mEventId!= null) {
                mEventId = mEventIdRow.insert(transaction);
            }
            final com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] medicationId = new com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] {mMedicationId };
            if (mMedicationId!= null) {
                mMedicationId = mMedicationIdRow.insert(transaction);
            }
            final MedicationEventTable.MedicationEventTablePrimaryKey primaryKey = new MedicationEventTable.MedicationEventTablePrimaryKey(transaction.insert(COLUMN_NAMES));
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
