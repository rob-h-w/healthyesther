
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
public class NoteEventTable
    extends Table
    implements Serializable
{

    public final static String EVENT_ID = "event_id";
    public final static String NOTE_ID = "note_id";

    @Nonnull
    @Override
    public String getName() {
        return "note_event";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS note_event ( \n    note_id   NOT NULL\n              REFERENCES note ( _id ) ON DELETE CASCADE\n                                      ON UPDATE CASCADE,\n    event_id  NOT NULL\n              REFERENCES event ( _id ) ON DELETE CASCADE\n                                       ON UPDATE CASCADE,\n    PRIMARY KEY ( note_id, event_id ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS note_event");
    }

    @Nonnull
    public NoteEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        try {
            final NoteEventTable.Row[] rows = new NoteEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new NoteEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public NoteEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        NoteEventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public NoteEventTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        NoteEventTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public NoteEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final NoteEventTable.Row[] rows = new NoteEventTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new NoteEventTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public NoteEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        NoteEventTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public NoteEventTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        NoteEventTable.Row row = select0Or1(database, where);
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

        private com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey mNoteId;

        public PrimaryKey(NoteEventTable.PrimaryKey other) {
            mEventId = other.mEventId;
            mNoteId = other.mNoteId;
        }

        public PrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey noteId) {
            mEventId = eventId;
            mNoteId = noteId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey getEventId() {
            return mEventId;
        }

        public void setNoteId(com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey noteId) {
            mNoteId = noteId;
        }

        public com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey getNoteId() {
            return mNoteId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof NoteEventTable.PrimaryKey)) {
                return false;
            }
            NoteEventTable.PrimaryKey thePrimaryKey = ((NoteEventTable.PrimaryKey) other);
            if (!(((mEventId == null)&&(thePrimaryKey.mEventId == null))||((mEventId!= null)&&mEventId.equals(thePrimaryKey.mEventId)))) {
                return false;
            }
            if (!(((mNoteId == null)&&(thePrimaryKey.mNoteId == null))||((mNoteId!= null)&&mNoteId.equals(thePrimaryKey.mNoteId)))) {
                return false;
            }
            return true;
        }

        @Nullable
        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(event_id = ");
            where.append(mEventId.getId());
            where.append(")");
            where.append(" AND (note_id = ");
            where.append(mNoteId.getId());
            where.append(")");
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class Row
        extends BaseRow<NoteEventTable.PrimaryKey>
        implements Serializable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.Row mNoteIdRow;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(2);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES_FOR_UPDATE.add("event_id");
            COLUMN_NAMES.add("note_id");
            COLUMN_NAMES_FOR_UPDATE.add("note_id");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setPrimaryKey(new NoteEventTable.PrimaryKey(((cursor.getLong("event_id")!= null)?new com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey(cursor.getLong("event_id")):null), ((cursor.getLong("note_id")!= null)?new com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey(cursor.getLong("note_id")):null)));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey noteId) {
            setPrimaryKey(new NoteEventTable.PrimaryKey(eventId, noteId));
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventId,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.NoteTable.Row noteId) {
            mEventIdRow = eventId;
            mNoteIdRow = noteId;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            NoteEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setEventId(mEventIdRow.getNextPrimaryKey());
                }
            }
            if (mNoteIdRow!= null) {
                mNoteIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setNoteId(mNoteIdRow.getNextPrimaryKey());
                }
            }
            if (((nextPrimaryKey == null)&&(mEventIdRow!= null))&&(mNoteIdRow!= null)) {
                NoteEventTable.PrimaryKey oldNextPrimaryKey = getNextPrimaryKey();
                setNextPrimaryKey(new NoteEventTable.PrimaryKey(mEventIdRow.getNextPrimaryKey(), mNoteIdRow.getNextPrimaryKey()));
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            NoteEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new NoteEventTable.PrimaryKey(getConcretePrimaryKey().getEventId(), getConcretePrimaryKey().getNoteId()));
                nextPrimaryKey = getNextPrimaryKey();
            }
            // This table does not use a row ID as a primary key.
            transaction.insert("note_event", COLUMN_NAMES, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getNoteId().getId());
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
            NoteEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("note_event", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getNoteId().getId());
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
            int actual = transaction.remove("note_event", getConcretePrimaryKey());
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
            if (!(other instanceof NoteEventTable.Row)) {
                return false;
            }
            NoteEventTable.Row theRow = ((NoteEventTable.Row) other);
            NoteEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            NoteEventTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            NoteEventTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            NoteEventTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isValid() {
            return true;
        }

    }

}
