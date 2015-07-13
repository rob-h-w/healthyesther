
package com.robwilliamson.healthyesther.db.generated;



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

        private long mEventId;
        private long mMedicationId;

        public boolean equals(MedicationEventTable.MedicationEventTablePrimaryKey medication_eventTablePrimaryKey) {
            if (medication_eventTablePrimaryKey == null) {
                return false;
            }
            if (medication_eventTablePrimaryKey == this) {
                return true;
            }
            if (medication_eventTablePrimaryKey.mMedicationId!= mMedicationId) {
                return false;
            }
            if (medication_eventTablePrimaryKey.mEventId!= mEventId) {
                return false;
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow) {
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
        }

    }

}
