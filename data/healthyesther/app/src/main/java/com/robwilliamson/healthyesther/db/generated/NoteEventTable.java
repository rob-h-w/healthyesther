
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.AndWhere;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class NoteEventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class NoteEventTablePrimaryKey
        implements Where
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey mNoteId;

        public NoteEventTablePrimaryKey(NoteEventTable.NoteEventTablePrimaryKey other) {
            mEventId = other.mEventId;
            mNoteId = other.mNoteId;
        }

        public NoteEventTablePrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId, com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey noteId) {
            mEventId = eventId;
            mNoteId = noteId;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            mEventId = eventId;
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setNoteId(com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey noteId) {
            mNoteId = noteId;
        }

        public com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey getNoteId() {
            return mNoteId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof NoteEventTable.NoteEventTablePrimaryKey)) {
                return false;
            }
            NoteEventTable.NoteEventTablePrimaryKey theNoteEventTablePrimaryKey = ((NoteEventTable.NoteEventTablePrimaryKey) other);
            if (theNoteEventTablePrimaryKey.mEventId!= mEventId) {
                if ((theNoteEventTablePrimaryKey.mEventId == null)||(!theNoteEventTablePrimaryKey.mEventId.equals(mEventId))) {
                    return false;
                }
            }
            if (theNoteEventTablePrimaryKey.mNoteId!= mNoteId) {
                if ((theNoteEventTablePrimaryKey.mNoteId == null)||(!theNoteEventTablePrimaryKey.mNoteId.equals(mNoteId))) {
                    return false;
                }
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
        extends BaseTransactable
    {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey mNoteId;
        public final static ArrayList COLUMN_NAMES = new ArrayList(2);
        private com.robwilliamson.healthyesther.db.generated.EventTable.Row mEventIdRow;
        private com.robwilliamson.healthyesther.db.generated.NoteTable.Row mNoteIdRow;

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("note_id");
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.NoteTable.Row noteTableRow) {
            mEventIdRow = eventTableRow;
            mNoteIdRow = noteTableRow;
        }

        public Row(
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey,
            @Nonnull
            com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey noteTablePrimaryKey) {
            mEventId = eventTablePrimaryKey;
            mNoteId = noteTablePrimaryKey;
        }

        public void setEventId(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventId) {
            if (((mEventId == null)&&(eventId == null))||((mEventId!= null)&&mEventId.equals(eventId))) {
                return ;
            }
            mEventId = eventId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey getEventId() {
            return mEventId;
        }

        public void setNoteId(com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey noteId) {
            if (((mNoteId == null)&&(noteId == null))||((mNoteId!= null)&&mNoteId.equals(noteId))) {
                return ;
            }
            mNoteId = noteId;
            setIsModified(true);
        }

        public com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey getNoteId() {
            return mNoteId;
        }

        @Override
        public Object insert(Transaction transaction) {
            final com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] eventId = new com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey[] {mEventId };
            if (mEventId == null) {
                eventId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey) mEventIdRow.insert(transaction));
            }
            final com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey[] noteId = new com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey[] {mNoteId };
            if (mNoteId == null) {
                noteId[ 0 ] = ((com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey) mNoteIdRow.insert(transaction));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, eventId[ 0 ], noteId[ 0 ]);
            final NoteEventTable.NoteEventTablePrimaryKey primaryKey = new NoteEventTable.NoteEventTablePrimaryKey(eventId[ 0 ], noteId[ 0 ]);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mEventId = eventId[ 0 ];
                    mNoteId = noteId[ 0 ];
                    setIsInDatabase(true);
                    setIsModified(false);
                }

            }
            );
            return primaryKey;
        }

        @Override
        public void remove(Transaction transaction) {
            if (!this.isInDatabase()) {
                return ;
            }
            int actual = transaction.remove(new AndWhere(mEventId, mNoteId));
            if (actual!= 1) {
                throw new BaseTransactable.RemoveFailed(1, actual);
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
            if (!(other instanceof NoteEventTable.Row)) {
                return false;
            }
            NoteEventTable.Row theRow = ((NoteEventTable.Row) other);
            if (!(((mEventId == null)&&(theRow.mEventId == null))||((mEventId!= null)&&mEventId.equals(theRow.mEventId)))) {
                return false;
            }
            if (!(((mNoteId == null)&&(theRow.mNoteId == null))||((mNoteId!= null)&&mNoteId.equals(theRow.mNoteId)))) {
                return false;
            }
            return true;
        }

    }

}
