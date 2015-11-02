
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class EventTable
    extends Table
    implements Serializable
{

    public final static String _ID = "_id";
    public final static String TYPE_ID = "type_id";
    public final static String CREATED = "created";
    public final static String WHEN = "when";
    public final static String MODIFIED = "modified";
    public final static String NAME = "name";

    @Nonnull
    @Override
    public String getName() {
        return "event";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE event ( \n    _id      INTEGER      PRIMARY KEY AUTOINCREMENT,\n    [when]   DATETIME     NOT NULL\n                          DEFAULT ( CURRENT_TIMESTAMP ),\n    created  DATETIME     NOT NULL\n                          DEFAULT ( CURRENT_TIMESTAMP ),\n    modified DATETIME,\n    type_id               REFERENCES event_type ( _id ) ON DELETE CASCADE\n                                                        ON UPDATE CASCADE\n                          NOT NULL,\n    name     TEXT( 140 )  DEFAULT ( '' ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS event");
    }

    @Nonnull
    public EventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        final EventTable.Row[] rows = new EventTable.Row[cursor.count()] ;
        int index = 0;
        if (cursor.count()> 0) {
            cursor.moveToFirst();
            do {
                rows[index ++] = new EventTable.Row(cursor);
            } while (cursor.moveToNext());
        }
        return rows;
    }

    @Nonnull
    public EventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        EventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public EventTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        EventTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(rows.length, where);
        }
        return rows[ 1 ];
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class PrimaryKey
        implements Serializable, Key
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

        @Nullable
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
    public static class Row
        extends BaseRow<EventTable.PrimaryKey>
        implements Serializable
    {

        @Nullable
        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey mTypeId;
        private com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row mTypeIdRow;
        @Nonnull
        private DateTime mCreated;
        @Nonnull
        private DateTime mWhen;
        @Nullable
        private DateTime mModified;
        @Nullable
        private String mName;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(6);
        public final static ArrayList<String> COLUMN_NAMES_FOR_INSERTION = new ArrayList<String>(5);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(5);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("type_id");
            COLUMN_NAMES_FOR_INSERTION.add("type_id");
            COLUMN_NAMES_FOR_UPDATE.add("type_id");
            COLUMN_NAMES.add("created");
            COLUMN_NAMES_FOR_INSERTION.add("created");
            COLUMN_NAMES_FOR_UPDATE.add("created");
            COLUMN_NAMES.add("when");
            COLUMN_NAMES_FOR_INSERTION.add("[when]");
            COLUMN_NAMES_FOR_UPDATE.add("[when]");
            COLUMN_NAMES.add("modified");
            COLUMN_NAMES_FOR_INSERTION.add("modified");
            COLUMN_NAMES_FOR_UPDATE.add("modified");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES_FOR_INSERTION.add("name");
            COLUMN_NAMES_FOR_UPDATE.add("name");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setTypeId(((cursor.getLong("type_id")!= null)?new com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey(cursor.getLong("type_id")):null));
            setCreated(cursor.getDateTime("created"));
            setWhen(cursor.getDateTime("when"));
            setModified(cursor.getDateTime("modified"));
            setName(cursor.getString("name"));
            setPrimaryKey(new EventTable.PrimaryKey(cursor.getLong("_id")));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey typeId,
            @Nonnull
            DateTime created,
            @Nonnull
            DateTime when,
            @Nullable
            DateTime modified,
            @Nullable
            String name) {
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
            DateTime when,
            @Nullable
            DateTime modified,
            @Nullable
            String name) {
            mTypeIdRow = typeId;
            mCreated = created;
            mWhen = when;
            mModified = modified;
            mName = name;
        }

        public void setTypeId(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey typeId) {
            if (((mTypeId == null)&&(typeId == null))||((mTypeId!= null)&&mTypeId.equals(typeId))) {
                return ;
            }
            mTypeId = typeId;
            setIsModified(true);
        }

        @Nonnull
        public com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey getTypeId() {
            return mTypeId;
        }

        public void setCreated(
            @Nonnull
            DateTime created) {
            if (((mCreated == null)&&(created == null))||((mCreated!= null)&&mCreated.equals(created))) {
                return ;
            }
            mCreated = created;
            setIsModified(true);
        }

        @Nonnull
        public DateTime getCreated() {
            return mCreated;
        }

        public void setWhen(
            @Nonnull
            DateTime when) {
            if (((mWhen == null)&&(when == null))||((mWhen!= null)&&mWhen.equals(when))) {
                return ;
            }
            mWhen = when;
            setIsModified(true);
        }

        @Nonnull
        public DateTime getWhen() {
            return mWhen;
        }

        public void setModified(
            @Nullable
            DateTime modified) {
            if (((mModified == null)&&(modified == null))||((mModified!= null)&&mModified.equals(modified))) {
                return ;
            }
            mModified = modified;
            setIsModified(true);
        }

        @Nullable
        public DateTime getModified() {
            return mModified;
        }

        public void setName(
            @Nullable
            String name) {
            if (((mName == null)&&(name == null))||((mName!= null)&&mName.equals(name))) {
                return ;
            }
            mName = name;
            setIsModified(true);
        }

        @Nullable
        public String getName() {
            return mName;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            if (mTypeIdRow!= null) {
                mTypeIdRow.applyTo(transaction);
                mTypeId = mTypeIdRow.getNextPrimaryKey();
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            final Object modified = ((mModified == null)?DateTime.class:mModified);
            final Object name = ((mName == null)?String.class:mName);
            EventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new EventTable.PrimaryKey(transaction.insert("event", COLUMN_NAMES_FOR_INSERTION, mTypeId.getId(), mCreated, mWhen, modified, name)));
                nextPrimaryKey = getNextPrimaryKey();
            } else {
                nextPrimaryKey.setId(transaction.insert("event", COLUMN_NAMES_FOR_INSERTION, mTypeId.getId(), mCreated, mWhen, modified, name));
            }
            // This table uses a row ID as a primary key.
            setIsModified(false);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    updatePrimaryKeyFromNext();
                }

            }
            );
            setIsInDatabase(true);
            return nextPrimaryKey;
        }

        @Override
        protected void update(
            @Nonnull
            Transaction transaction) {
            if (!isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            applyToRows(transaction);
            final Object modified = ((mModified == null)?DateTime.class:mModified);
            final Object name = ((mName == null)?String.class:mName);
            int actual = transaction.update("event", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, mTypeId.getId(), mCreated, mWhen, modified, name);
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed(1, actual);
            }
            setIsModified(false);
        }

        @Override
        protected void remove(
            @Nonnull
            Transaction transaction) {
            if ((!isInDatabase())||isDeleted()) {
                return ;
            }
            int actual = transaction.remove("event", getConcretePrimaryKey());
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.RemoveFailed(1, actual);
            }
            setIsDeleted(true);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(false);
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
