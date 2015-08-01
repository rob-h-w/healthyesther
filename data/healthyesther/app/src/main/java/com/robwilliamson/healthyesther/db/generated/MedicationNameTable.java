
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

        private com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey mMedicationId;
        private String mName;

        public MedicationNameTablePrimaryKey(MedicationNameTable.MedicationNameTablePrimaryKey other) {
            mMedicationId = other.mMedicationId;
            mName = other.mName;
        }

        public MedicationNameTablePrimaryKey(com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationId, String name) {
            mMedicationId = medicationId;
            mName = name;
        }

        public void setMedicationId(com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationId) {
            mMedicationId = medicationId;
        }

        public com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey getMedicationId() {
            return mMedicationId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationNameTable.MedicationNameTablePrimaryKey)) {
                return false;
            }
            MedicationNameTable.MedicationNameTablePrimaryKey theMedicationNameTablePrimaryKey = ((MedicationNameTable.MedicationNameTablePrimaryKey) other);
            if (theMedicationNameTablePrimaryKey.mMedicationId!= mMedicationId) {
                if ((theMedicationNameTablePrimaryKey.mMedicationId == null)||(!theMedicationNameTablePrimaryKey.mMedicationId.equals(mMedicationId))) {
                    return false;
                }
            }
            if (theMedicationNameTablePrimaryKey.mName!= mName) {
                if ((theMedicationNameTablePrimaryKey.mName == null)||(!theMedicationNameTablePrimaryKey.mName.equals(mName))) {
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
    public final static class Row {

        private com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey mMedicationId;
        private String mName;

        public Row(MedicationNameTable.MedicationNameTablePrimaryKey name, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow) {
        }

        public Row(MedicationNameTable.MedicationNameTablePrimaryKey name, com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
        }

        public void setMedicationId(com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationId) {
            mMedicationId = medicationId;
        }

        public com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey getMedicationId() {
            return mMedicationId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

    }

}
