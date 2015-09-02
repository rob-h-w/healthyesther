
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Transaction;


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
    public final static class NoteTablePrimaryKey implements Key
    {

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
    public final static class Row
        extends BaseRow<NoteTable.NoteTablePrimaryKey>
    {

        private String mNote;
        private String mName;
        private NoteTable.NoteTablePrimaryKey mId;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("note");
        }

        public Row(
            @Nonnull
            String name, NoteTable.NoteTablePrimaryKey id, String note) {
            mName = name;
            mId = id;
            mNote = note;
        }

        public void setId(NoteTable.NoteTablePrimaryKey id) {
            if (((mId == null)&&(id == null))||((mId!= null)&&mId.equals(id))) {
                return ;
            }
            mId = id;
            setIsModified(true);
        }

        public NoteTable.NoteTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            if (((mName == null)&&(name == null))||((mName!= null)&&mName.equals(name))) {
                return ;
            }
            mName = name;
            setIsModified(true);
        }

        public String getName() {
            return mName;
        }

        public void setNote(String note) {
            if (((mNote == null)&&(note == null))||((mNote!= null)&&mNote.equals(note))) {
                return ;
            }
            mNote = note;
            setIsModified(true);
        }

        public String getNote() {
            return mNote;
        }

        @Override
        public Object insert(Transaction transaction) {
            final long rowId = transaction.insert(COLUMN_NAMES, mId, mName, mNote);
            final NoteTable.NoteTablePrimaryKey primaryKey = new NoteTable.NoteTablePrimaryKey(rowId);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    mId = primaryKey;
                    setIsInDatabase(true);
                    setIsModified(false);
                }

            }
            );
            return primaryKey;
        }

        @Override
        public void update(Transaction transaction) {
            if (!this.isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(mId, COLUMN_NAMES, mId, mName, mNote);
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed(1, actual);
            }
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    setIsModified(false);
                }

            }
            );
        }

        @Override
        public void remove(Transaction transaction) {
            if (!this.isInDatabase()) {
                return ;
            }
            int actual = transaction.remove(mId);
            if (actual!= 1) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.RemoveFailed(1, actual);
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
            if (!(other instanceof NoteTable.Row)) {
                return false;
            }
            NoteTable.Row theRow = ((NoteTable.Row) other);
            if (!(((mId == null)&&(theRow.mId == null))||((mId!= null)&&mId.equals(theRow.mId)))) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(((mNote == null)&&(theRow.mNote == null))||((mNote!= null)&&mNote.equals(theRow.mNote)))) {
                return false;
            }
            return true;
        }

    }

}
