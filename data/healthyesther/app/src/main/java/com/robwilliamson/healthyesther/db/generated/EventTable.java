
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class EventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class EventTablePrimaryKey {

        private long mId;

        public EventTablePrimaryKey(EventTable.EventTablePrimaryKey other) {
            mId = other.mId;
        }

        public EventTablePrimaryKey(long id) {
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
            if (!(other instanceof EventTable.EventTablePrimaryKey)) {
                return false;
            }
            EventTable.EventTablePrimaryKey theEventTablePrimaryKey = ((EventTable.EventTablePrimaryKey) other);
            if (theEventTablePrimaryKey.mId!= mId) {
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

        private DateTime mModified;
        private String mName;
        private DateTime mCreated;
        private DateTime mWhen;
        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey mTypeId;
        private EventTable.EventTablePrimaryKey mId;
        private EventTable.EventTablePrimaryKey mPrimaryKey = null;
        public final static ArrayList COLUMN_NAMES = new ArrayList(6);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("created");
            COLUMN_NAMES.add("modified");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("type_id");
            COLUMN_NAMES.add("when");
        }

        public Row(DateTime created, com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row eventTypeTableRow, DateTime when, EventTable.EventTablePrimaryKey id, DateTime modified, String name) {
        }

        public Row(DateTime created, com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey eventTypeTablePrimaryKey, DateTime when, EventTable.EventTablePrimaryKey id, DateTime modified, String name) {
        }

        public void setId(EventTable.EventTablePrimaryKey id) {
            mId = id;
        }

        public EventTable.EventTablePrimaryKey getId() {
            return mId;
        }

        public void setCreated(DateTime created) {
            mCreated = created;
        }

        public DateTime getCreated() {
            return mCreated;
        }

        public void setModified(DateTime modified) {
            mModified = modified;
        }

        public DateTime getModified() {
            return mModified;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setTypeId(com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey typeId) {
            mTypeId = typeId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey getTypeId() {
            return mTypeId;
        }

        public void setWhen(DateTime when) {
            mWhen = when;
        }

        public DateTime getWhen() {
            return mWhen;
        }

        @Override
        public void insert(Transaction transaction) {
        }

    }

}
