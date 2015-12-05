
package com.robwilliamson.healthyesther.db.generated;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public class HealthScoreTable
    extends Table
    implements Serializable
{

    public final static String _ID = "_id";
    public final static String BEST_VALUE = "best_value";
    public final static String NAME = "name";
    public final static String RANDOM_QUERY = "random_query";
    public final static String MAX_LABEL = "max_label";
    public final static String MIN_LABEL = "min_label";

    @Nonnull
    @Override
    public String getName() {
        return "health_score";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS health_score ( \n    _id          INTEGER      PRIMARY KEY AUTOINCREMENT,\n    name         TEXT( 140 )  NOT NULL\n                              UNIQUE,\n    best_value   INTEGER      NOT NULL,\n    random_query BOOLEAN      NOT NULL\n                              DEFAULT ( 0 ),\n    min_label    TEXT( 140 ),\n    max_label    TEXT( 140 ) \n)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS health_score");
    }

    @Nonnull
    public HealthScoreTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        final HealthScoreTable.Row[] rows = new HealthScoreTable.Row[cursor.count()] ;
        int index = 0;
        if (cursor.count()> 0) {
            cursor.moveToFirst();
            do {
                rows[index ++] = new HealthScoreTable.Row(cursor);
            } while (cursor.moveToNext());
        }
        return rows;
    }

    @Nonnull
    public HealthScoreTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        HealthScoreTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public HealthScoreTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        HealthScoreTable.Row[] rows = select(database, where);
        if (rows.length == 0) {
            return null;
        }
        if (rows.length > 1) {
            throw new Table.TooManyRowsException(rows.length, where);
        }
        return rows[ 0 ];
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class PrimaryKey
        implements Serializable, Key
    {

        private long mId;

        public PrimaryKey(HealthScoreTable.PrimaryKey other) {
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
            if (!(other instanceof HealthScoreTable.PrimaryKey)) {
                return false;
            }
            HealthScoreTable.PrimaryKey thePrimaryKey = ((HealthScoreTable.PrimaryKey) other);
            if (!(mId == thePrimaryKey.mId)) {
                return false;
            }
            return true;
        }

        @Nullable
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
    public static class Row
        extends BaseRow<HealthScoreTable.PrimaryKey>
        implements Serializable
    {

        private long mBestValue;
        @Nonnull
        private String mName;
        private boolean mRandomQuery;
        @Nullable
        private String mMaxLabel;
        @Nullable
        private String mMinLabel;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(6);
        public final static ArrayList<String> COLUMN_NAMES_FOR_INSERTION = new ArrayList<String>(5);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(5);

        static {
            COLUMN_NAMES.add("_id");
            COLUMN_NAMES.add("best_value");
            COLUMN_NAMES_FOR_INSERTION.add("best_value");
            COLUMN_NAMES_FOR_UPDATE.add("best_value");
            COLUMN_NAMES.add("name");
            COLUMN_NAMES_FOR_INSERTION.add("name");
            COLUMN_NAMES_FOR_UPDATE.add("name");
            COLUMN_NAMES.add("random_query");
            COLUMN_NAMES_FOR_INSERTION.add("random_query");
            COLUMN_NAMES_FOR_UPDATE.add("random_query");
            COLUMN_NAMES.add("max_label");
            COLUMN_NAMES_FOR_INSERTION.add("max_label");
            COLUMN_NAMES_FOR_UPDATE.add("max_label");
            COLUMN_NAMES.add("min_label");
            COLUMN_NAMES_FOR_INSERTION.add("min_label");
            COLUMN_NAMES_FOR_UPDATE.add("min_label");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setBestValue(cursor.getLong("best_value"));
            setName(cursor.getString("name"));
            setRandomQuery(cursor.getBoolean("random_query"));
            setMaxLabel(cursor.getString("max_label"));
            setMinLabel(cursor.getString("min_label"));
            setPrimaryKey(new HealthScoreTable.PrimaryKey(cursor.getLong("_id")));
            setIsInDatabase(true);
        }

        public Row(long bestValue,
            @Nonnull
            String name, boolean randomQuery,
            @Nullable
            String maxLabel,
            @Nullable
            String minLabel) {
            mBestValue = bestValue;
            mName = name;
            mRandomQuery = randomQuery;
            mMaxLabel = maxLabel;
            mMinLabel = minLabel;
        }

        public void setBestValue(long bestValue) {
            if (mBestValue == bestValue) {
                return ;
            }
            mBestValue = bestValue;
            setIsModified(true);
        }

        public long getBestValue() {
            return mBestValue;
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

        @Nonnull
        public String getName() {
            return mName;
        }

        public void setRandomQuery(boolean randomQuery) {
            if (mRandomQuery == randomQuery) {
                return ;
            }
            mRandomQuery = randomQuery;
            setIsModified(true);
        }

        public boolean getRandomQuery() {
            return mRandomQuery;
        }

        public void setMaxLabel(
            @Nullable
            String maxLabel) {
            if (((mMaxLabel == null)&&(maxLabel == null))||((mMaxLabel!= null)&&mMaxLabel.equals(maxLabel))) {
                return ;
            }
            mMaxLabel = maxLabel;
            setIsModified(true);
        }

        @Nullable
        public String getMaxLabel() {
            return mMaxLabel;
        }

        public void setMinLabel(
            @Nullable
            String minLabel) {
            if (((mMinLabel == null)&&(minLabel == null))||((mMinLabel!= null)&&mMinLabel.equals(minLabel))) {
                return ;
            }
            mMinLabel = minLabel;
            setIsModified(true);
        }

        @Nullable
        public String getMinLabel() {
            return mMinLabel;
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            final Object maxLabel = ((mMaxLabel == null)?String.class:mMaxLabel);
            final Object minLabel = ((mMinLabel == null)?String.class:mMinLabel);
            HealthScoreTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            if (nextPrimaryKey == null) {
                setNextPrimaryKey(new HealthScoreTable.PrimaryKey(transaction.insert("health_score", COLUMN_NAMES_FOR_INSERTION, mBestValue, mName, mRandomQuery, maxLabel, minLabel)));
                nextPrimaryKey = getNextPrimaryKey();
            } else {
                nextPrimaryKey.setId(transaction.insert("health_score", COLUMN_NAMES_FOR_INSERTION, mBestValue, mName, mRandomQuery, maxLabel, minLabel));
            }
            // This table uses a row ID as a primary key.
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
            final Object maxLabel = ((mMaxLabel == null)?String.class:mMaxLabel);
            final Object minLabel = ((mMinLabel == null)?String.class:mMinLabel);
            int actual = transaction.update("health_score", getConcretePrimaryKey(), COLUMN_NAMES_FOR_UPDATE, mBestValue, mName, mRandomQuery, maxLabel, minLabel);
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
            int actual = transaction.remove("health_score", getConcretePrimaryKey());
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
            if (!(other instanceof HealthScoreTable.Row)) {
                return false;
            }
            HealthScoreTable.Row theRow = ((HealthScoreTable.Row) other);
            if (!(mBestValue == theRow.mBestValue)) {
                return false;
            }
            if (!(((mName == null)&&(theRow.mName == null))||((mName!= null)&&mName.equals(theRow.mName)))) {
                return false;
            }
            if (!(mRandomQuery == theRow.mRandomQuery)) {
                return false;
            }
            if (!(((mMaxLabel == null)&&(theRow.mMaxLabel == null))||((mMaxLabel!= null)&&mMaxLabel.equals(theRow.mMaxLabel)))) {
                return false;
            }
            if (!(((mMinLabel == null)&&(theRow.mMinLabel == null))||((mMinLabel!= null)&&mMinLabel.equals(theRow.mMinLabel)))) {
                return false;
            }
            HealthScoreTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            HealthScoreTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            HealthScoreTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            HealthScoreTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isValid() {
            if ((mName == null)||mName.equals("")) {
                return false;
            }
            return true;
        }

    }

}
