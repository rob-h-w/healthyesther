
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
public final class EventTable
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

        public PrimaryKey(EventTable.PrimaryKey other) {
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
            if (!(other instanceof EventTable.PrimaryKey)) {
                return false;
            }
            EventTable.PrimaryKey thePrimaryKey = ((EventTable.PrimaryKey) other);
            if (!(mId == thePrimaryKey.mId)) {
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
        extends BaseRow<EventTable.PrimaryKey>
    {

        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey mTypeId;
        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row mTypeIdRow;
        @Nonnull
        private DateTime mCreated;
        @Nonnull
        private DateTime mWhen;
        private DateTime mModified;
        private String mName;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(6);
        public final static ArrayList<String> INSERT_LIST = new ArrayList<String>(5);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("type_id");
            INSERT_LIST.add("type_id");
            COLUMN_NAMES.add("created");
            INSERT_LIST.add("created");
            COLUMN_NAMES.add("when");
            INSERT_LIST.add("when");
            COLUMN_NAMES.add("modified");
            INSERT_LIST.add("modified");
            COLUMN_NAMES.add("name");
            INSERT_LIST.add("name");
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey typeId,
            @Nonnull
            DateTime created,
            @Nonnull
            DateTime when, DateTime modified, String name) {
            mTypeId = typeId;
            mCreated = created;
            mWhen = when;
            mModified = modified;
            mName = name;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row typeId,
            @Nonnull
            DateTime created,
            @Nonnull
            DateTime when, DateTime modified, String name) {
            mTypeIdRow = typeId;
            mCreated = created;
            mWhen = when;
            mModified = modified;
            mName = name;
        }

        public void setTypeId(com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey typeId) {
            if (((mTypeId == null)&&(typeId == null))||((mTypeId!= null)&&mTypeId.equals(typeId))) {
                return ;
            }
            mTypeId = typeId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey getTypeId() {
            return mTypeId;
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

        @Override
        protected Object insert(Transaction transaction) {
            if (mTypeIdRow!= null) {
                mTypeIdRow.applyTo(transaction);
                mTypeId = mTypeIdRow.getNextPrimaryKey();
            }
            EventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new EventTable.PrimaryKey(transaction.insert(INSERT_LIST, mTypeId.getId(), mCreated, mWhen, mModified, mName)));
            } else {
                nextPrimaryKey.setId(transaction.insert(INSERT_LIST, mTypeId.getId(), mCreated, mWhen, mModified, mName));
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
        protected void update(Transaction transaction) {
            if (!this.isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES, mTypeId, mCreated, mWhen, mModified, mName);
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
        protected void remove(Transaction transaction) {
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
            if (!(other instanceof EventTable.Row)) {
                return false;
            }
            EventTable.Row theRow = ((EventTable.Row) other);
            if (!(((mTypeId == null)&&(theRow.mTypeId == null))||((mTypeId!= null)&&mTypeId.equals(theRow.mTypeId)))) {
                return false;
            }
            if (!(((mCreated == null)&&(theRow.mCreated == null))||((mCreated!= null)&&mCreated.equals(theRow.mCreated)))) {
                return false;
            }
            if (!(((mWhen == null)&&(theRow.mWhen == null))||((mWhen!= null)&&mWhen.equals(theRow.mWhen)))) {
                return false;
            }
            if (!(((mModified == null)&&(theRow.mModified == null))||((mModified!= null)&&mModified.equals(theRow.mModified)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            EventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            EventTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            EventTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            EventTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
