
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
    public final static class PrimaryKey
        implements Key
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
            if (thePrimaryKey.mId!= mId) {
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
        extends BaseRow<NoteTable.PrimaryKey>
    {

        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(3);
        private NoteTable.PrimaryKey mId;
        @Nonnull
        private String mName;
        private String mNote;

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES.add("note");
        }

        public Row(
            @Nonnull
            String name, String note) {
            setPrimaryKey(new NoteTable.PrimaryKey());
            mName = name;
            mNote = note;
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
            getConcretePrimaryKey();
            NoteTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            boolean constructPrimaryKey = (!(primaryKey == null));
            if (constructPrimaryKey) {
                setPrimaryKey(new NoteTable.PrimaryKey(primaryKey.getId(), rowId));
                primaryKey = setPrimaryKey(new NoteTable.PrimaryKey(primaryKey.getId(), rowId));
            }
            final long rowId = transaction.insert(COLUMN_NAMES, primaryKey.getId(), mName, mNote);
            final NoteTable.PrimaryKey primaryKey = primaryKey;
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    primaryKey.setId(rowId);
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
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES, mName, mNote);
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
            int actual = transaction.remove(getConcretePrimaryKey());
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
