
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MedicationNameTable
    extends Table
{


    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE medication_name ( \n    medication_id             REFERENCES medication ( _id ) ON DELETE CASCADE\n                                                            ON UPDATE CASCADE,\n    name          TEXT( 50 )  NOT NULL,\n    PRIMARY KEY ( medication_id, name ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS medication_name");
    }


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
            if (!(((mName == null)&&(thePrimaryKey.mName == null))||((mName!= null)&&mName.equals(thePrimaryKey.mName)))) {
                return false;
            }
            if (!(((mMedicationId == null)&&(thePrimaryKey.mMedicationId == null))||((mMedicationId!= null)&&mMedicationId.equals(thePrimaryKey.mMedicationId)))) {
                return false;
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

        @Nonnull
        private String mName;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey mMedicationId;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(0);

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

        private void applyToRows(Transaction transaction) {
            if (mMedicationIdRow!= null) {
                mMedicationIdRow.applyTo(transaction);
                mMedicationId = mMedicationIdRow.getNextPrimaryKey();
            }
        }

        @Override
        protected Object insert(Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            // This table does not use a row ID as a primary key.
            setNextPrimaryKey(new MedicationNameTable.PrimaryKey(mName, mMedicationId));
            MedicationNameTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            transaction.insert(COLUMN_NAMES, nextPrimaryKey.getName(), nextPrimaryKey.getMedicationId().getId());
            setIsModified(false);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(true);
                    updatePrimaryKeyFromNext();
                }

            }
            );
            return nextPrimaryKey;
        }

        @Override
        protected void update(Transaction transaction) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void remove(Transaction transaction) {
            if ((!isInDatabase())||isDeleted()) {
                return ;
            }
            int actual = transaction.remove(getConcretePrimaryKey());
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.RemoveFailed(1, actual);
            }
            setIsDeleted(true);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(false);
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
            MedicationNameTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            MedicationNameTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            MedicationNameTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            MedicationNameTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
