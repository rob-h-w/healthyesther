
package com.robwilliamson.healthyesther.db.generated;



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

        private long mEventId;
        private long mNoteId;

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
                return false;
            }
            if (theNoteEventTablePrimaryKey.mNoteId!= mNoteId) {
                return false;
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.NoteTable.Row noteTableRow) {
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.NoteTable.NoteTablePrimaryKey noteTablePrimaryKey) {
        }

    }

}
