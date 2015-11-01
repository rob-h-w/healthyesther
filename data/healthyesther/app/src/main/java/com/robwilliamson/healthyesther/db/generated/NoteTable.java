
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class NoteTable
    extends Table
    implements Serializable
{

    public final static String _ID = "_id";
    public final static String NAME = "name";
    public final static String NOTE = "note";

    @Nonnull
    @Override
    public String getName() {
        return "note";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE note ( \n    _id  INTEGER      PRIMARY KEY,\n    name TEXT( 140 )  NOT NULL,\n    note TEXT \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS note");
    }

    @Nonnull
    public NoteTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        final NoteTable.Row[] rows = new NoteTable.Row[cursor.count()] ;
        int index = 0;
        if (cursor.count()> 0) {
            cursor.moveToFirst();
            do {
                rows[index ++] = new NoteTable.Row(cursor);
            } while (cursor.moveToNext());
        }
        return rows;
    }

    @Nonnull
    public NoteTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        NoteTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public NoteTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        NoteTable.Row[] rows = select(database, where);
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

        public PrimaryKey(NoteTable.PrimaryKey other) {
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
            if (!(other instanceof NoteTable.PrimaryKey)) {
                return false;
            }
            NoteTable.PrimaryKey thePrimaryKey = ((NoteTable.PrimaryKey) other);
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
    public static class Row
        extends BaseRow<NoteTable.PrimaryKey>
        implements Serializable
    {

        @Nonnull
        private String mName;
        @Nullable
        private String mNote;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        public final static ArrayList<String> COLUMN_NAMES_FOR_INSERTION = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(2);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES_FOR_INSERTION.add("name");
            COLUMN_NAMES_FOR_UPDATE.add("name");
            COLUMN_NAMES.add("note");
            COLUMN_NAMES_FOR_INSERTION.add("note");
            COLUMN_NAMES_FOR_UPDATE.add("note");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setName(cursor.getString("name"));
            setNote(cursor.getString("note"));
            setPrimaryKey(new NoteTable.PrimaryKey(cursor.getLong("_id")));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            String name,
            @Nullable
            String note) {
            mName = name;
            mNote = note;
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

        public void setNote(
            @Nullable
            String note) {
            if (((mNote == null)&&(note == null))||((mNote!= null)&&mNote.equals(note))) {
                return ;
            }
            mNote = note;
            setIsModified(true);
        }

        @Nullable
        public String getNote() {
            return mNote;
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            final Object note = ((mNote == null)?String.class:mNote);
            NoteTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new NoteTable.PrimaryKey(transaction.insert("note", COLUMN_NAMES_FOR_INSERTION, mName, note)));
                nextPrimaryKey = getNextPrimaryKey();
            } else {
                nextPrimaryKey.setId(transaction.insert("note", COLUMN_NAMES_FOR_INSERTION, mName, note));
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
            final Object note = ((mNote == null)?String.class:mNote);
            int actual = transaction.update("note", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, mName, note);
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
            int actual = transaction.remove("note", getConcretePrimaryKey());
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
            if (!(other instanceof NoteTable.Row)) {
                return false;
            }
            NoteTable.Row theRow = ((NoteTable.Row) other);
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(((mNote == null)&&(theRow.mNote == null))||((mNote!= null)&&mNote.equals(theRow.mNote)))) {
                return false;
            }
            NoteTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            NoteTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            NoteTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            NoteTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
