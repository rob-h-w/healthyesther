
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
public final class EventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class EventTablePrimaryKey
        implements Where
    {

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
        extends BaseTransactable
    {

        private DateTime mModified;
        private String mName;
        private DateTime mCreated;
        private DateTime mWhen;
        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey mTypeId;
        private EventTable.EventTablePrimaryKey mId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(6);
        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row mTypeIdRow;

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("created");
            COLUMN_NAMES.add("modified");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("type_id");
            COLUMN_NAMES.add("when");
        }

        public Row(
            @Nonnull
            DateTime created,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row eventTypeTableRow,
            @Nonnull
            DateTime when, EventTable.EventTablePrimaryKey id, DateTime modified, String name) {
            mCreated = created;
            mTypeIdRow = eventTypeTableRow;
            mWhen = when;
            mId = id;
            mModified = modified;
            mName = name;
        }

        public Row(
            @Nonnull
            DateTime created,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey eventTypeTablePrimaryKey,
            @Nonnull
            DateTime when, EventTable.EventTablePrimaryKey id, DateTime modified, String name) {
            mCreated = created;
            mTypeId = eventTypeTablePrimaryKey;
            mWhen = when;
            mId = id;
            mModified = modified;
            mName = name;
        }

        public void setId(EventTable.EventTablePrimaryKey id) {
            if (((mId == null)&&(id == null))||((mId!= null)&&mId.equals(id))) {
                return ;
            }
            mId = id;
            setIsModified(true);
        }

        public EventTable.EventTablePrimaryKey getId() {
            return mId;
        }

        public void setCreated(DateTime created) {
            if (((mCreated == null)&&(created == null))||((mCreated!= null)&&mCreated.equals(created))) {
                return ;
            }
            mCreated = created;
            setIsModified(true);
        }

        public DateTime getCreated() {
            return mCreated;
        }

        public void setModified(DateTime modified) {
            if (((mModified == null)&&(modified == null))||((mModified!= null)&&mModified.equals(modified))) {
                return ;
            }
            mModified = modified;
            setIsModified(true);
        }

        public DateTime getModified() {
            return mModified;
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

        public void setTypeId(com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey typeId) {
            if (((mTypeId == null)&&(typeId == null))||((mTypeId!= null)&&mTypeId.equals(typeId))) {
                return ;
            }
            mTypeId = typeId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey getTypeId() {
            return mTypeId;
        }

        public void setWhen(DateTime when) {
            if (((mWhen == null)&&(when == null))||((mWhen!= null)&&mWhen.equals(when))) {
                return ;
            }
            mWhen = when;
            setIsModified(true);
        }

        public DateTime getWhen() {
            return mWhen;
        }

        @Override
        public Object insert(Transaction transaction) {
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mCreated, mModified, mName, mTypeId, mWhen);
            final EventTable.EventTablePrimaryKey primaryKey = new EventTable.EventTablePrimaryKey(rowId);
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
            if (!(other instanceof EventTable.Row)) {
                return false;
            }
            EventTable.Row theRow = ((EventTable.Row) other);
            if (!(((mId == null)&&(theRow.mId == null))||((mId!= null)&&mId.equals(theRow.mId)))) {
                return false;
            }
            if (!(((mCreated == null)&&(theRow.mCreated == null))||((mCreated!= null)&&mCreated.equals(theRow.mCreated)))) {
                return false;
            }
            if (!(((mModified == null)&&(theRow.mModified == null))||((mModified!= null)&&mModified.equals(theRow.mModified)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(((mTypeId == null)&&(theRow.mTypeId == null))||((mTypeId!= null)&&mTypeId.equals(theRow.mTypeId)))) {
                return false;
            }
            if (!(((mWhen == null)&&(theRow.mWhen == null))||((mWhen!= null)&&mWhen.equals(theRow.mWhen)))) {
                return false;
            }
            return true;
        }

    }

}
