
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MealTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MealTablePrimaryKey {

        private long mId;

        public MealTablePrimaryKey(MealTable.MealTablePrimaryKey other) {
            mId = other.mId;
        }

        public MealTablePrimaryKey(long id) {
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
            if (!(other instanceof MealTable.MealTablePrimaryKey)) {
                return false;
            }
            MealTable.MealTablePrimaryKey theMealTablePrimaryKey = ((MealTable.MealTablePrimaryKey) other);
            if (theMealTablePrimaryKey.mId!= mId) {
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
        private MealTable.MealTablePrimaryKey mId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(2);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
        }

        public Row(
            @Nonnull
            String name, MealTable.MealTablePrimaryKey id) {
            mName = name;
            mId = id;
        }

        public void setId(MealTable.MealTablePrimaryKey id) {
            mId = id;
        }

        public MealTable.MealTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        @Override
        public Object insert(Transaction transaction) {
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mName);
            final MealTable.MealTablePrimaryKey primaryKey = new MealTable.MealTablePrimaryKey(rowId);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mId = primaryKey;
                }

            }
            );
            return primaryKey;
        }

    }

}
