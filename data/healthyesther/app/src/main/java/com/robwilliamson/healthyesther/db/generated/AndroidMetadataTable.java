
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
public class AndroidMetadataTable
    extends Table
    implements Serializable
{

    public final static String LOCALE = "locale";

    @Nonnull
    @Override
    public String getName() {
        return "android_metadata";
    }

    @Override
    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT)");
    }

    @Override
    public void drop(Transaction transaction) {
        transaction.execSQL("DROP TABLE IF EXISTS android_metadata");
    }

    @Nonnull
    public AndroidMetadataTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        final Cursor cursor = database.select(where, this);
        final AndroidMetadataTable.Row[] rows = new AndroidMetadataTable.Row[cursor.count()] ;
        int index = 0;
        if (cursor.count()> 0) {
            cursor.moveToFirst();
            do {
                rows[index ++] = new AndroidMetadataTable.Row(cursor);
            } while (cursor.moveToNext());
        }
        return rows;
    }

    @Nonnull
    public AndroidMetadataTable.Row[] select(
        @Nonnull
        Database database,
        @Nonnull
        AndroidMetadataTable.PrimaryKey where) {
        return select(database, ((Where) where));
    }

    @Nullable
    public AndroidMetadataTable.Row select0Or1(
        @Nonnull
        Database database,
        @Nonnull
        Where where) {
        AndroidMetadataTable.Row[] rows = select(database, where);
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


        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof AndroidMetadataTable.PrimaryKey)) {
                return false;
            }
            return true;
        }

        @Nullable
        @Override
        public String getWhere() {
            StringBuilder where = new StringBuilder();
            return where.toString();
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public static class Row
        extends BaseRow<AndroidMetadataTable.PrimaryKey>
        implements Serializable
    {

        @Nullable
        private String mLocale;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(1);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(1);

        static {
            COLUMN_NAMES.add("locale");
            COLUMN_NAMES_FOR_UPDATE.add("locale");
        }

        public Row(
            @Nonnull
            Cursor cursor) {
            setLocale(cursor.getString("locale"));
            setPrimaryKey(new AndroidMetadataTable.PrimaryKey());
            setIsInDatabase(true);
        }

        public Row(
            @Nullable
            String locale) {
            setPrimaryKey(new AndroidMetadataTable.PrimaryKey());
            mLocale = locale;
        }

        public void setLocale(
            @Nullable
            String locale) {
            if (((mLocale == null)&&(locale == null))||((mLocale!= null)&&mLocale.equals(locale))) {
                return ;
            }
            mLocale = locale;
            setIsModified(true);
        }

        @Nullable
        public String getLocale() {
            return mLocale;
        }

        @Nonnull
        @Override
        protected Object insert(
            @Nonnull
            Transaction transaction) {
            final Object locale = ((mLocale == null)?String.class:mLocale);
            transaction.insert("android_metadata", COLUMN_NAMES, locale);
            return transaction.insert("android_metadata", COLUMN_NAMES, locale);
        }

        @Override
        protected void update(
            @Nonnull
            Transaction transaction) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void remove(
            @Nonnull
            Transaction transaction) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof AndroidMetadataTable.Row)) {
                return false;
            }
            AndroidMetadataTable.Row theRow = ((AndroidMetadataTable.Row) other);
            if (!(((mLocale == null)&&(theRow.mLocale == null))||((mLocale!= null)&&mLocale.equals(theRow.mLocale)))) {
                return false;
            }
            AndroidMetadataTable.PrimaryKey nextPrimaryKey = getNextPrimaryKey();
            AndroidMetadataTable.PrimaryKey otherNextPrimaryKey = theRow.getNextPrimaryKey();
            if (!(((nextPrimaryKey == null)&&(otherNextPrimaryKey == null))||((nextPrimaryKey!= null)&&nextPrimaryKey.equals(otherNextPrimaryKey)))) {
                return false;
            }
            AndroidMetadataTable.PrimaryKey primaryKey = getConcretePrimaryKey();
            AndroidMetadataTable.PrimaryKey otherPrimaryKey = theRow.getConcretePrimaryKey();
            if (!(((primaryKey == null)&&(otherPrimaryKey == null))||((primaryKey!= null)&&primaryKey.equals(otherPrimaryKey)))) {
                return false;
            }
            return true;
        }

    }

}
