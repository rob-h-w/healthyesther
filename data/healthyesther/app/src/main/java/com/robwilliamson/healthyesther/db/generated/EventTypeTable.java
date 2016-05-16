
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Order;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class EventTypeTable
    extends Table
    implements Serializable
{

    public final static String _ID = "_id";
    public final static String NAME = "name";
    public final static String ICON = "icon";

    @Nonnull
    @Override
    public String getName() {
        return "event_type";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS event_type ( \n    _id  INTEGER     PRIMARY KEY AUTOINCREMENT,\n    name TEXT( 30 )  NOT NULL\n                     UNIQUE,\n    icon TEXT \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS event_type");
    }

    @Nonnull
    public EventTypeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        try {
            final EventTypeTable.Row[] rows = new EventTypeTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new EventTypeTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public EventTypeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        EventTypeTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public EventTypeTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        EventTypeTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public EventTypeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final EventTypeTable.Row[] rows = new EventTypeTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new EventTypeTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public EventTypeTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        EventTypeTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public EventTypeTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        EventTypeTable.Row row = select0Or1(database, where);
        if (row == null) {
            throw new Table.NotFoundException(this, where);
        }
        return row;
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class PrimaryKey
        implements Serializable, Key
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

        @Override
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
        extends BaseRow<EventTypeTable.PrimaryKey>
        implements Serializable
    {

        @Nonnull
        private String mName;
        @Nullable
        private String mIcon;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        public final static ArrayList<String> COLUMN_NAMES_FOR_INSERTION = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(3);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES_FOR_UPDATE.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES_FOR_INSERTION.add("name");
            COLUMN_NAMES_FOR_UPDATE.add("name");
            COLUMN_NAMES.add("icon");
            COLUMN_NAMES_FOR_INSERTION.add("icon");
            COLUMN_NAMES_FOR_UPDATE.add("icon");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setName(cursor.getString("name"));
            setIcon(cursor.getString("icon"));
            setPrimaryKey(new EventTypeTable.PrimaryKey(cursor.getLong("_id")));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            String name,
            @Nullable
            String icon) {
            mName = name;
            mIcon = icon;
        }

        public void setName(
            @Nonnull
            String name) {
            if (((mName == null)&&(name == null))||((mName!= null)&&mName.equals(name))) {
                return ;
            }
            mName = name;
            setIsModified(true);
        }

        @Nonnull
        public String getName() {
            return mName;
        }

        public void setIcon(
            @Nullable
            String icon) {
            if (((mIcon == null)&&(icon == null))||((mIcon!= null)&&mIcon.equals(icon))) {
                return ;
            }
            mIcon = icon;
            setIsModified(true);
        }

        @Nullable
        public String getIcon() {
            return mIcon;
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            final Object icon = ((mIcon == null)?String.class:mIcon);
            EventTypeTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new EventTypeTable.PrimaryKey(transaction.insert("event_type", COLUMN_NAMES_FOR_INSERTION, mName, icon)));
                nextPrimaryKey = getNextPrimaryKey();
            } else {
                nextPrimaryKey.setId(transaction.insert("event_type", COLUMN_NAMES_FOR_INSERTION, mName, icon));
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
            final Object icon = ((mIcon == null)?String.class:mIcon);
            EventTypeTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("event_type", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getId(), mName, icon);
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
            int actual = transaction.remove("event_type", getConcretePrimaryKey());
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

        @Override
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
            EventTypeTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            EventTypeTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            EventTypeTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            EventTypeTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isValid() {
            if ((mName == null)||mName.equals("")) {
                return false;
            }
            if (mName.length()> 30) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = EventTypeTable.Row.class.getCanonicalName().hashCode();
            hash = (hash^mName.hashCode());
            hash = ((mIcon == null)?hash:(hash^mIcon.hashCode()));
            EventTypeTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            if (primaryKey!= null) {
                hash = (hash^Long.valueOf(primaryKey.getId()).hashCode());
            }
            return hash;
        }

    }

}
