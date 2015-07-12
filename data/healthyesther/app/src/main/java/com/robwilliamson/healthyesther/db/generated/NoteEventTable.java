
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

        public boolean equals(NoteEventTable.NoteEventTablePrimaryKey note_eventTablePrimaryKey) {
            if (note_eventTablePrimaryKey == null) {
                return false;
            }
            if (note_eventTablePrimaryKey == this) {
                return true;
            }
            if (note_eventTablePrimaryKey.mNoteId == mNoteId) {
            }
            if (note_eventTablePrimaryKey.mEventId == mEventId) {
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
