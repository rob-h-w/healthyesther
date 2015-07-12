
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MedicationTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MedicationTablePrimaryKey {

        private long mId;

        public boolean equals(MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
            if (medicationTablePrimaryKey == null) {
                return false;
            }
            if (medicationTablePrimaryKey == this) {
                return true;
            }
            if (medicationTablePrimaryKey.mId == mId) {
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(String medicationName, Long medicationId) {
        }

    }

}
