
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
public final class EventTypeTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class EventTypeTablePrimaryKey
        implements Where
    {

        private long mId;

        public EventTypeTablePrimaryKey(EventTypeTable.EventTypeTablePrimaryKey other) {
            mId = other.mId;
        }

        public EventTypeTablePrimaryKey(long id) {
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
            if (!(other instanceof EventTypeTable.EventTypeTablePrimaryKey)) {
                return false;
            }
            EventTypeTable.EventTypeTablePrimaryKey theEventTypeTablePrimaryKey = ((EventTypeTable.EventTypeTablePrimaryKey) other);
            if (theEventTypeTablePrimaryKey.mId!= mId) {
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

        private String mIcon;
        private String mName;
        private EventTypeTable.EventTypeTablePrimaryKey mId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(3);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("icon");
            COLUMN_NAMES.add("name");
        }

        public Row(
            @Nonnull
            String name, EventTypeTable.EventTypeTablePrimaryKey id, String icon) {
            mName = name;
            mId = id;
            mIcon = icon;
        }

        public void setId(EventTypeTable.EventTypeTablePrimaryKey id) {
            if (((mId == null)&&(id == null))||((mId!= null)&&mId.equals(id))) {
                return ;
            }
            mId = id;
            setIsModified(true);
        }

        public EventTypeTable.EventTypeTablePrimaryKey getId() {
            return mId;
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
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mIcon, mName);
            final EventTypeTable.EventTypeTablePrimaryKey primaryKey = new EventTypeTable.EventTypeTablePrimaryKey(rowId);
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
            if (!(other instanceof EventTypeTable.Row)) {
                return false;
            }
            EventTypeTable.Row theRow = ((EventTypeTable.Row) other);
            if (!(((mId == null)&&(theRow.mId == null))||((mId!= null)&&mId.equals(theRow.mId)))) {
                return false;
            }
            if (!(((mIcon == null)&&(theRow.mIcon == null))||((mIcon!= null)&&mIcon.equals(theRow.mIcon)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            return true;
        }

    }

}
