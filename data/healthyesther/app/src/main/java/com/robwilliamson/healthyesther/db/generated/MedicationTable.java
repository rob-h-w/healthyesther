
package com.robwilliamson.healthyesther.db.generated;

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
public final class MedicationTable
    extends Table
{

    public final static String _ID = "_id";
    public final static String NAME = "name";

    @Nonnull
    @Override
    public String getName() {
        return "medication";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE medication ( \n    _id  INTEGER     PRIMARY KEY AUTOINCREMENT,\n    name TEXT( 50 )  NOT NULL \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS medication");
    }

    @Nonnull
    public MedicationTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        final MedicationTable.Row[] rows = new MedicationTable.Row[cursor.count()] ;
        int index = 0;
        cursor.moveToFirst();
        do {
            rows[index ++] = new MedicationTable.Row(cursor);
        } while (cursor.moveToNext());
        return rows;
    }

    @Nonnull
    public MedicationTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MedicationTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Key
    {

        private long mId;

        public PrimaryKey(MedicationTable.PrimaryKey other) {
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
            if (!(other instanceof MedicationTable.PrimaryKey)) {
                return false;
            }
            MedicationTable.PrimaryKey thePrimaryKey = ((MedicationTable.PrimaryKey) other);
            if (!(mId == thePrimaryKey.mId)) {
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
        extends BaseRow<MedicationTable.PrimaryKey>
    {

        @Nonnull
        private String mName;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_INSERTION = new ArrayList<String>(1);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(1);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES_FOR_INSERTION.add("name");
            COLUMN_NAMES_FOR_UPDATE.add("name");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setName(cursor.getString("name"));
            setPrimaryKey(new MedicationTable.PrimaryKey(cursor.getLong("_id")));
        }

        public Row(
            @Nonnull
            String name) {
            mName = name;
        }

        public void setName(
            @Nonnull
            String name) {
            if (((mName == null)&&(name == null))||((mName!= null)&&mName.equals(name))) {
                return ;
            }
            mName = name;
            setIsModified(true);
        }

        public String getName() {
            return mName;
        }

        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            MedicationTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new MedicationTable.PrimaryKey(transaction.insert(COLUMN_NAMES_FOR_INSERTION, mName)));
            } else {
                nextPrimaryKey.setId(transaction.insert(COLUMN_NAMES_FOR_INSERTION, mName));
            }
            // This table uses a row ID as a primary key.
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
            if (!isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            int actual = transaction.update(getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, mName);
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
            if (!(other instanceof MedicationTable.Row)) {
                return false;
            }
            MedicationTable.Row theRow = ((MedicationTable.Row) other);
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            MedicationTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            MedicationTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            MedicationTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            MedicationTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
