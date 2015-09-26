
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
public final class MedicationTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Key
    {

        private long mId;

        public PrimaryKey(MedicationTable.PrimaryKey other) {
            mId = other.mId;
        }

        public PrimaryKey(long id) {
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
            if (!(other instanceof MedicationTable.PrimaryKey)) {
                return false;
            }
            MedicationTable.PrimaryKey thePrimaryKey = ((MedicationTable.PrimaryKey) other);
            if (thePrimaryKey.mId!= mId) {
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


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row
        extends BaseRow<MedicationTable.PrimaryKey>
    {

        @Nonnull
        private String mName;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
        }

        public Row(
            @Nonnull
            String name) {
            mName = name;
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
            MedicationTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new MedicationTable.PrimaryKey(transaction.insert(COLUMN_NAMES, nextPrimaryKey.getId(), mName)));
            }
            // This table uses a row ID as a primary key.
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(true);
                    setIsModified(false);
                    updatePrimaryKeyFromNext();
                }

            }
            );
            return nextPrimaryKey;
        }

        @Override
        public void update(Transaction transaction) {
            if (!this.isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES, mName);
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
            if (!(other instanceof MedicationTable.Row)) {
                return false;
            }
            MedicationTable.Row theRow = ((MedicationTable.Row) other);
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            MedicationTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            MedicationTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            MedicationTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            MedicationTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
