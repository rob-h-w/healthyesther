
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
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
    public final static class EventTypeTablePrimaryKey {

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

        public Row(String name, EventTypeTable.EventTypeTablePrimaryKey id, String icon) {
        }

        public void setId(EventTypeTable.EventTypeTablePrimaryKey id) {
            mId = id;
        }

        public EventTypeTable.EventTypeTablePrimaryKey getId() {
            return mId;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        @Override
        public Object insert(Transaction transaction) {
            final EventTypeTable.EventTypeTablePrimaryKey primaryKey = new EventTypeTable.EventTypeTablePrimaryKey(transaction.insert(COLUMN_NAMES, mIcon, mName));
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
