
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;


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
    public final static class NoteEventTablePrimaryKey {

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

        static {
            COLUMN_NAMES.add("event_id");
            COLUMN_NAMES.add("note_id");
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.NoteTable.Row noteTableRow) {
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey noteTablePrimaryKey) {
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

        @Override
        public void insert(Transaction transaction) {
        }

    }

}
