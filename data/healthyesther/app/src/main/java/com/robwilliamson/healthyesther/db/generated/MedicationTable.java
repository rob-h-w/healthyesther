
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

        public MedicationTablePrimaryKey(MedicationTable.MedicationTablePrimaryKey other) {
            mId = other.mId;
        }

        public MedicationTablePrimaryKey(long Id) {
            mId = Id;
        }

        public void setId(long Id) {
            mId = Id;
        }

        public long getId() {
            return mId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationTable.MedicationTablePrimaryKey)) {
                return false;
            }
            MedicationTable.MedicationTablePrimaryKey theMedicationTablePrimaryKey = ((MedicationTable.MedicationTablePrimaryKey) other);
            if (theMedicationTablePrimaryKey.mId!= mId) {
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


        public Row(String medicationName, Long medicationId) {
        }

    }

}
