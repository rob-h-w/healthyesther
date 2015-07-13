
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class NoteTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class NoteTablePrimaryKey {

        private long mId;

        public boolean equals(NoteTable.NoteTablePrimaryKey noteTablePrimaryKey) {
            if (noteTablePrimaryKey == null) {
                return false;
            }
            if (noteTablePrimaryKey == this) {
                return true;
            }
            if (noteTablePrimaryKey.mId!= mId) {
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


        public Row(String noteName, String noteNote, Long noteId) {
        }

    }

}
