
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class UnitsTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row
        extends BaseTransactable
    {

        private double mSiFactor;
        private String mName;
        private UnitsTable.UnitsTablePrimaryKey mId;
        private UnitsTable.UnitsTablePrimaryKey mPrimaryKey = null;
        public final static ArrayList COLUMN_NAMES = new ArrayList(3);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("si_factor");
        }

        public Row(String name, UnitsTable.UnitsTablePrimaryKey id, Double siFactor) {
        }

        public void setId(UnitsTable.UnitsTablePrimaryKey id) {
            mId = id;
        }

        public UnitsTable.UnitsTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setSiFactor(double siFactor) {
            mSiFactor = siFactor;
        }

        public double getSiFactor() {
            return mSiFactor;
        }

        @Override
        public void insert(Transaction transaction) {
            final UnitsTable.UnitsTablePrimaryKey primaryKey = new UnitsTable.UnitsTablePrimaryKey(transaction.insert(COLUMN_NAMES, mName, mSiFactor));
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mPrimaryKey = primaryKey;
                }

            }
            );
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class UnitsTablePrimaryKey {

        private long mId;

        public UnitsTablePrimaryKey(UnitsTable.UnitsTablePrimaryKey other) {
            mId = other.mId;
        }

        public UnitsTablePrimaryKey(long id) {
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
            if (!(other instanceof UnitsTable.UnitsTablePrimaryKey)) {
                return false;
            }
            UnitsTable.UnitsTablePrimaryKey theUnitsTablePrimaryKey = ((UnitsTable.UnitsTablePrimaryKey) other);
            if (theUnitsTablePrimaryKey.mId!= mId) {
                return false;
            }
            return true;
        }

    }

}
