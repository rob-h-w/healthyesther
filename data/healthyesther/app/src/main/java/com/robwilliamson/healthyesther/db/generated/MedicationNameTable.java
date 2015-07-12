
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MedicationNameTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MedicationNameTablePrimaryKey {

        private long mMedicationId;
        private String mName;

        public boolean equals(MedicationNameTable.MedicationNameTablePrimaryKey medication_nameTablePrimaryKey) {
            if (medication_nameTablePrimaryKey == null) {
                return false;
            }
            if (medication_nameTablePrimaryKey == this) {
                return true;
            }
            if (medication_nameTablePrimaryKey.mName == mName) {
            }
            if (medication_nameTablePrimaryKey.mMedicationId == mMedicationId) {
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(String medicationNameName, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow) {
        }

        public Row(String medicationNameName, com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
        }

    }

}
