
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
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
public final class NoteEventTable
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
        transaction.execSQL("CREATE TABLE note_event ( \n    note_id   NOT NULL\n              REFERENCES note ( _id ) ON DELETE CASCADE\n                                      ON UPDATE CASCADE,\n    event_id  NOT NULL\n              REFERENCES event ( _id ) ON DELETE CASCADE\n                                       ON UPDATE CASCADE,\n    PRIMARY KEY ( note_id, event_id ) \n)");
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
        final NoteEventTable.Row[] rows = new NoteEventTable.Row[cursor.count()] ;
        int index = 0;
        cursor.moveToFirst();
        do {
            rows[index ++] = new NoteEventTable.Row(cursor);
        } while (cursor.moveToNext());
        return rows;
    }

    @Nonnull
    public NoteEventTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        NoteEventTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
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
    public final static class Row
        extends BaseRow<NoteEventTable.PrimaryKey>
        implements Serializable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey mNoteId;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.Row mNoteIdRow;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(0);

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("note_id");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setPrimaryKey(new NoteEventTable.PrimaryKey(((cursor.getLong("event_id")!= null)?new com.robwilliamson.healthyesther.db.generated.EventTable.PrimaryKey(cursor.getLong("event_id")):null), ((cursor.getLong("note_id")!= null)?new com.robwilliamson.healthyesther.db.generated.NoteTable.PrimaryKey(cursor.getLong("note_id")):null)));
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
            if (mEventIdRow!= null) {
                mEventIdRow.applyTo(transaction);
                mEventId = mEventIdRow.getNextPrimaryKey();
            }
            if (mNoteIdRow!= null) {
                mNoteIdRow.applyTo(transaction);
                mNoteId = mNoteIdRow.getNextPrimaryKey();
            }
        }

        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            // This table does not use a row ID as a primary key.
            setNextPrimaryKey(new NoteEventTable.PrimaryKey(mEventId, mNoteId));
            NoteEventTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            transaction.insert(COLUMN_NAMES, nextPrimaryKey.getEventId().getId(), nextPrimaryKey.getNoteId().getId());
            setIsModified(false);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsInDatabase(true);
                    updatePrimaryKeyFromNext();
                }

            }
            );
            return nextPrimaryKey;
        }

        @Override
        protected void update(
            @Nonnull
            Transaction transaction) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void remove(
            @Nonnull
            Transaction transaction) {
            if ((!isInDatabase())||isDeleted()) {
                return ;
            }
            int actual = transaction.remove(getConcretePrimaryKey());
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

    }

}
