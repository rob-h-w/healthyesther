
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
public final class MedicationNameTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Key
    {

        private String mName;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey mMedicationId;

        public PrimaryKey(MedicationNameTable.PrimaryKey other) {
            mName = other.mName;
            mMedicationId = other.mMedicationId;
        }

        public PrimaryKey(String name, com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            mName = name;
            mMedicationId = medicationId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
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
            if (!(other instanceof MedicationNameTable.PrimaryKey)) {
                return false;
            }
            MedicationNameTable.PrimaryKey thePrimaryKey = ((MedicationNameTable.PrimaryKey) other);
            if (thePrimaryKey.mName!= mName) {
                if ((thePrimaryKey.mName == null)||(!thePrimaryKey.mName.equals(mName))) {
                    return false;
                }
            }
            if (thePrimaryKey.mMedicationId!= mMedicationId) {
                if ((thePrimaryKey.mMedicationId == null)||(!thePrimaryKey.mMedicationId.equals(mMedicationId))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(name = ");
            where.append(mName);
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
        extends BaseRow<MedicationNameTable.PrimaryKey>
    {

        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        @Nonnull
        private String mName;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey mMedicationId;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;

        static {
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("medication_id");
        }

        public Row(
            @Nonnull
            String name, com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            setPrimaryKey(new MedicationNameTable.PrimaryKey(name, medicationId));
        }

        public Row(
            @Nonnull
            String name, com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationId) {
            mName = name;
            mMedicationIdRow = medicationId;
        }

        @Override
        public Object insert(Transaction transaction) {
            MedicationNameTable.PrimaryKey primaryKey = getNextPrimaryKey();
            final boolean constructPrimaryKey = (primaryKey!= null);
            if (constructPrimaryKey) {
                mMedicationIdRow.applyTo(transaction);
                setNextPrimaryKey(new MedicationNameTable.PrimaryKey(mName, mMedicationIdRow.getNextPrimaryKey()));
                primaryKey = getNextPrimaryKey();
            }
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(true);
                    setIsModified(false);
                    updatePrimaryKeyFromNext();
                }

            }
            );
            return primaryKey;
        }

        @Override
        public void update(Transaction transaction) {
            if (!this.isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES);
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed(1, actual);
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
            if (!(other instanceof MedicationNameTable.Row)) {
                return false;
            }
            MedicationNameTable.Row theRow = ((MedicationNameTable.Row) other);
            return true;
        }

    }

}
