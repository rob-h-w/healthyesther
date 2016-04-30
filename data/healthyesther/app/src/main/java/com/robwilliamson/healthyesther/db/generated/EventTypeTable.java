
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
public final class EventTypeTable
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

        public PrimaryKey(EventTypeTable.PrimaryKey other) {
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
            if (!(other instanceof EventTypeTable.PrimaryKey)) {
                return false;
            }
            EventTypeTable.PrimaryKey thePrimaryKey = ((EventTypeTable.PrimaryKey) other);
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
        extends BaseRow<EventTypeTable.PrimaryKey>
    {

        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        private EventTypeTable.PrimaryKey mId;
        @Nonnull
        private String mName;
        private String mIcon;

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("icon");
        }

        public Row(
            @Nonnull
            String name, String icon) {
            setPrimaryKey(new EventTypeTable.PrimaryKey());
            mName = name;
            mIcon = icon;
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

        public void setIcon(String icon) {
            if (((mIcon == null)&&(icon == null))||((mIcon!= null)&&mIcon.equals(icon))) {
                return ;
            }
            mIcon = icon;
            setIsModified(true);
        }

        public String getIcon() {
            return mIcon;
        }

        @Override
        public Object insert(Transaction transaction) {
            getConcretePrimaryKey();
            EventTypeTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            boolean constructPrimaryKey = (!(primaryKey == null));
            if (constructPrimaryKey) {
                setPrimaryKey(new EventTypeTable.PrimaryKey(primaryKey.getId(), rowId));
                primaryKey = setPrimaryKey(new EventTypeTable.PrimaryKey(primaryKey.getId(), rowId));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, primaryKey.getId(), mName, mIcon);
            final EventTypeTable.PrimaryKey primaryKey = primaryKey;
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    primaryKey.setId(rowId);
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
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES, mName, mIcon);
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
            if (!(other instanceof EventTypeTable.Row)) {
                return false;
            }
            EventTypeTable.Row theRow = ((EventTypeTable.Row) other);
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(((mIcon == null)&&(theRow.mIcon == null))||((mIcon!= null)&&mIcon.equals(theRow.mIcon)))) {
                return false;
            }
            return true;
        }

    }

}
