
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
public final class MedicationNameTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MedicationNameTablePrimaryKey
        implements Where
    {

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

        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(medication_id = ");
            where.append(mMedicationId.getId());
            where.append(")");
            where.append(" AND (name = ");
            where.append(mName);
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

        private com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey mMedicationId;
        private String mName;
        public final static ArrayList COLUMN_NAMES = new ArrayList(2);
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;

        static {
            COLUMN_NAMES.add("medication_id");
            COLUMN_NAMES.add("name");
        }

        public Row(
            @Nonnull
            String name, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationTableRow) {
            mName = name;
            mMedicationIdRow = medicationTableRow;
        }

        public Row(
            @Nonnull
            String name, com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey medicationTablePrimaryKey) {
            mName = name;
            mMedicationId = medicationTablePrimaryKey;
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

        public void setName(String name) {
            if (((mName == null)&&(name == null))||((mName!= null)&&mName.equals(name))) {
                return ;
            }
            mName = name;
            setIsModified(true);
        }

        public String getName() {
            return mName;
        }

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] medicationId = new com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey[] {mMedicationId };
            if (mMedicationId == null) {
                medicationId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.MedicationTable.MedicationTablePrimaryKey) mMedicationIdRow.insert(transaction));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, medicationId[ 0 ], mName);
            final MedicationNameTable.MedicationNameTablePrimaryKey primaryKey = new MedicationNameTable.MedicationNameTablePrimaryKey(medicationId[ 0 ], mName);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
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
            int actual = transaction.update(new AndWhere(mMedicationId, new Where() {


                public String getWhere() {
                    return ("name = "+ mName);
                }

            }
            ), COLUMN_NAMES, mMedicationId, mName);
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
            int actual = transaction.remove(new AndWhere(mMedicationId, new Where() {


                public String getWhere() {
                    return ("name = "+ mName);
                }

            }
            ));
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
            if (!(other instanceof MedicationNameTable.Row)) {
                return false;
            }
            MedicationNameTable.Row theRow = ((MedicationNameTable.Row) other);
            if (!(((mMedicationId == null)&&(theRow.mMedicationId == null))||((mMedicationId!= null)&&mMedicationId.equals(theRow.mMedicationId)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            return true;
        }

    }

}
