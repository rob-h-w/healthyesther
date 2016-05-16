
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Order;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class MedicationNameTable
    extends Table
    implements Serializable
{

    public final static String NAME = "name";
    public final static String MEDICATION_ID = "medication_id";

    @Nonnull
    @Override
    public String getName() {
        return "medication_name";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS medication_name ( \n    medication_id             REFERENCES medication ( _id ) ON DELETE CASCADE\n                                                            ON UPDATE CASCADE,\n    name          TEXT( 50 )  NOT NULL,\n    PRIMARY KEY ( medication_id, name ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS medication_name");
    }

    @Nonnull
    public MedicationNameTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        try {
            final MedicationNameTable.Row[] rows = new MedicationNameTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new MedicationNameTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public MedicationNameTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MedicationNameTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public MedicationNameTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        MedicationNameTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(this, rows.length, where);
        }
        return rows[ 0 ];
    }

    @Nonnull
    public MedicationNameTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where,
        @Nonnull
        Order order) {
        final Cursor cursor = database.select(where, this, order);
        try {
            final MedicationNameTable.Row[] rows = new MedicationNameTable.Row[cursor.count()] ;
            int index = 0;
            if (cursor.count()> 0) {
                cursor.moveToFirst();
                do {
                    rows[index ++] = new MedicationNameTable.Row(cursor);
                } while (cursor.moveToNext());
            }
            return rows;
        } finally {
            cursor.close();
        }
    }

    @Nonnull
    public MedicationNameTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        MedicationNameTable.PrimaryKey where,
        @Nonnull
        Order order) {
        return select(database, ((Where) where), order);
    }

    @Nonnull
    public MedicationNameTable.Row select1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        MedicationNameTable.Row row = select0Or1(database, where);
        if (row == null) {
            throw new Table.NotFoundException(this, where);
        }
        return row;
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class PrimaryKey
        implements Serializable, Key
    {

        private String mName;
        private com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey mMedicationId;

        public PrimaryKey(MedicationNameTable.PrimaryKey other) {
            mName = other.mName;
            mMedicationId = other.mMedicationId;
        }

        public PrimaryKey(String name, com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            mName = name;
            mMedicationId = medicationId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setMedicationId(com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            mMedicationId = medicationId;
        }

        public com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey getMedicationId() {
            return mMedicationId;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationNameTable.PrimaryKey)) {
                return false;
            }
            MedicationNameTable.PrimaryKey thePrimaryKey = ((MedicationNameTable.PrimaryKey) other);
            if (!(((mName == null)&&(thePrimaryKey.mName == null))||((mName!= null)&&mName.equals(thePrimaryKey.mName)))) {
                return false;
            }
            if (!(((mMedicationId == null)&&(thePrimaryKey.mMedicationId == null))||((mMedicationId!= null)&&mMedicationId.equals(thePrimaryKey.mMedicationId)))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = MedicationNameTable.PrimaryKey.class.getCanonicalName().hashCode();
            hash = (hash^mName.hashCode());
            hash = ((mMedicationId == null)?hash:(hash^mMedicationId.hashCode()));
            return hash;
        }

        @Nullable
        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            where.append("(name = ");
            where.append(mName);
            where.append(")");
            where.append(" AND (medication_id = ");
            where.append(mMedicationId.getId());
            where.append(")");
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class Row
        extends BaseRow<MedicationNameTable.PrimaryKey>
        implements Serializable
    {

        private com.robwilliamson.healthyesther.db.generated.MedicationTable.Row mMedicationIdRow;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(2);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(2);

        static {
            COLUMN_NAMES.add("name");
            COLUMN_NAMES_FOR_UPDATE.add("name");
            COLUMN_NAMES.add("medication_id");
            COLUMN_NAMES_FOR_UPDATE.add("medication_id");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setPrimaryKey(new MedicationNameTable.PrimaryKey(cursor.getString("name"), ((cursor.getLong("medication_id")!= null)?new com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey(cursor.getLong("medication_id")):null)));
            setIsInDatabase(true);
        }

        public Row(
            @Nonnull
            String name,
            @Nullable
            com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId) {
            setPrimaryKey(new MedicationNameTable.PrimaryKey(name, medicationId));
        }

        public Row(
            @Nonnull
            String name,
            @Nullable
            com.robwilliamson.healthyesther.db.generated.MedicationTable.Row medicationId) {
            mMedicationIdRow = medicationId;
        }

        private void applyToRows(
            @Nonnull
            Transaction transaction) {
            MedicationNameTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (mMedicationIdRow!= null) {
                mMedicationIdRow.applyTo(transaction);
                if (nextPrimaryKey!= null) {
                    nextPrimaryKey.setMedicationId(mMedicationIdRow.getNextPrimaryKey());
                }
            }
            if ((nextPrimaryKey == null)&&(mMedicationIdRow!= null)) {
                MedicationNameTable.PrimaryKey oldNextPrimaryKey = getNextPrimaryKey();
                setNextPrimaryKey(new MedicationNameTable.PrimaryKey(oldNextPrimaryKey.getName(), mMedicationIdRow.getNextPrimaryKey()));
            }
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            // Ensure all keys are updated from any rows passed.
            applyToRows(transaction);
            MedicationNameTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new MedicationNameTable.PrimaryKey(getConcretePrimaryKey().getName(), getConcretePrimaryKey().getMedicationId()));
                nextPrimaryKey = getNextPrimaryKey();
            }
            // This table does not use a row ID as a primary key.
            transaction.insert("medication_name", COLUMN_NAMES, nextPrimaryKey.getName(), nextPrimaryKey.getMedicationId().getId());
            setIsModified(false);
            transaction.addCompletionHandler(new Transaction.CompletionHandler() {


                public void onCompleted() {
                    updatePrimaryKeyFromNext();
                }

            }
            );
            setIsInDatabase(true);
            return nextPrimaryKey;
        }

        @Override
        protected void update(
            @Nonnull
            Transaction transaction) {
            if (!isInDatabase()) {
                throw new com.robwilliamson.healthyesther.db.includes.BaseTransactable.UpdateFailed("Could not update because the row is not in the database.");
            }
            applyToRows(transaction);
            MedicationNameTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            int actual = transaction.update("medication_name", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, nextPrimaryKey.getName(), nextPrimaryKey.getMedicationId().getId());
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
            int actual = transaction.remove("medication_name", getConcretePrimaryKey());
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

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MedicationNameTable.Row)) {
                return false;
            }
            MedicationNameTable.Row theRow = ((MedicationNameTable.Row) other);
            MedicationNameTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            MedicationNameTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            MedicationNameTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            MedicationNameTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public void loadRelations(Database database) {
            com.robwilliamson.healthyesther.db.generated.MedicationTable.PrimaryKey medicationId = getConcretePrimaryKey().getMedicationId();
            if (medicationId!= null) {
                if (mMedicationIdRow == null) {
                    mMedicationIdRow = HealthDatabase.MEDICATION_TABLE.select0Or1(database, medicationId);
                }
            }
        }

        @Nullable
        public com.robwilliamson.healthyesther.db.generated.MedicationTable.Row getMedicationRow() {
            return mMedicationIdRow;
        }

        @Override
        public int hashCode() {
            int hash = MedicationNameTable.Row.class.getCanonicalName().hashCode();
            MedicationNameTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            if (primaryKey!= null) {
                hash = (hash^primaryKey.getName().hashCode());
                hash = ((primaryKey.getMedicationId() == null)?hash:(hash^primaryKey.getMedicationId().hashCode()));
            }
            return hash;
        }

    }

}
