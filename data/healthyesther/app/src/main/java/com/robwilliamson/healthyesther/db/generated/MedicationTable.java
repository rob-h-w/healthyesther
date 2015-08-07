
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


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

        public MedicationTablePrimaryKey(long id) {
            mId = id;
        }

        public void setId(long id) {
            mId = id;
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
    public final static class Row
        extends BaseTransactable
    {

        private String mName;
        private MedicationTable.MedicationTablePrimaryKey mId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(2);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
        }

        public Row(String name, MedicationTable.MedicationTablePrimaryKey id) {
        }

        public void setId(MedicationTable.MedicationTablePrimaryKey id) {
            mId = id;
        }

        public MedicationTable.MedicationTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        @Override
        public void insert(Transaction transaction) {
        }

    }

}
