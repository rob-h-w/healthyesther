
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
public final class MealTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MealTablePrimaryKey
        implements Where
    {

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
            if (((mId == null)&&(id == null))||((mId!= null)&&mId.equals(id))) {
                return ;
            }
            mId = id;
            setIsModified(true);
        }

        public MealTable.MealTablePrimaryKey getId() {
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

        @Override
        public Object insert(Transaction transaction) {
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mName);
            final MealTable.MealTablePrimaryKey primaryKey = new MealTable.MealTablePrimaryKey(rowId);
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

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MealTable.Row)) {
                return false;
            }
            MealTable.Row theRow = ((MealTable.Row) other);
            if (!(((mId == null)&&(theRow.mId == null))||((mId!= null)&&mId.equals(theRow.mId)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            return true;
        }

    }

}
