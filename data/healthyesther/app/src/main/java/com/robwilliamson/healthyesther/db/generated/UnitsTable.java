
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


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
        public final static ArrayList COLUMN_NAMES = new ArrayList(3);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("si_factor");
        }

        public Row(
            @Nonnull
            String name, UnitsTable.UnitsTablePrimaryKey id, double siFactor) {
            mName = name;
            mId = id;
            mSiFactor = siFactor;
        }

        public void setId(UnitsTable.UnitsTablePrimaryKey id) {
            if (((mId == null)&&(id == null))||((mId!= null)&&mId.equals(id))) {
                return ;
            }
            mId = id;
            setIsModified(true);
        }

        public UnitsTable.UnitsTablePrimaryKey getId() {
            return mId;
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

        public void setSiFactor(double siFactor) {
            if (mSiFactor == siFactor) {
                return ;
            }
            mSiFactor = siFactor;
            setIsModified(true);
        }

        public double getSiFactor() {
            return mSiFactor;
        }

        @Override
        public Object insert(Transaction transaction) {
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mName, mSiFactor);
            final UnitsTable.UnitsTablePrimaryKey primaryKey = new UnitsTable.UnitsTablePrimaryKey(rowId);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mId = primaryKey;
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
            int actual = transaction.update(mId, COLUMN_NAMES, mId, mName, mSiFactor);
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
            int actual = transaction.remove(mId);
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
            if (!(other instanceof UnitsTable.Row)) {
                return false;
            }
            UnitsTable.Row theRow = ((UnitsTable.Row) other);
            if (!(((mId == null)&&(theRow.mId == null))||((mId!= null)&&mId.equals(theRow.mId)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(mSiFactor == theRow.mSiFactor)) {
                return false;
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class UnitsTablePrimaryKey implements Where
    {

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

        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(_id = ");
            where.append(mId);
            where.append(")");
            return where.toString();
        }

    }

}
