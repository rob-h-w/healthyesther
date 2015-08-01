
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

        public NoteTablePrimaryKey(NoteTable.NoteTablePrimaryKey other) {
            mId = other.mId;
        }

        public NoteTablePrimaryKey(long id) {
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
            if (!(other instanceof NoteTable.NoteTablePrimaryKey)) {
                return false;
            }
            NoteTable.NoteTablePrimaryKey theNoteTablePrimaryKey = ((NoteTable.NoteTablePrimaryKey) other);
            if (theNoteTablePrimaryKey.mId!= mId) {
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

        private String mNote;
        private String mName;
        private NoteTable.NoteTablePrimaryKey mId;

        public Row(String name, NoteTable.NoteTablePrimaryKey id, String note) {
        }

        public void setId(NoteTable.NoteTablePrimaryKey id) {
            mId = id;
        }

        public NoteTable.NoteTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setNote(String note) {
            mNote = note;
        }

        public String getNote() {
            return mNote;
        }

    }

}
