
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


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
    public final static class Row
        extends BaseTransactable
    {

        private com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey mMedicationId;
        private String mName;
        public final static ArrayList COLUMN_NAMES = new ArrayList(2);
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;

        static {
            COLUMN_NAMES.add("medication_id");
            COLUMN_NAMES.add("name");
        }

        public Row(MedicationNameTable.MedicationNameTablePrimaryKey name, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row rowMedicationId) {
            mMedicationIdRow = rowMedicationId;
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

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] medicationId = new com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] {mMedicationId };
            if (mMedicationId!= null) {
                medicationId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey) mMedicationIdRow.insert(transaction));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, medicationId[ 0 ], mName);
            final MedicationNameTable.MedicationNameTablePrimaryKey primaryKey = new MedicationNameTable.MedicationNameTablePrimaryKey(medicationId[ 0 ]);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mMedicationId = medicationId[ 0 ];
                }

            }
            );
            return primaryKey;
        }

    }

}
