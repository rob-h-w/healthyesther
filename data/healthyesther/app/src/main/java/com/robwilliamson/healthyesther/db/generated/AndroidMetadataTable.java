
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class AndroidMetadataTable
    implements Table
{


    public void create(Transaction transaction) {
        transaction.execSQL("CREATE TABLE android_metadata (locale TEXT)");
    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class PrimaryKey
        implements Key
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
    public final static class Row
        extends BaseRow<AndroidMetadataTable.PrimaryKey>
    {

        private String mLocale;
        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(1);
        public final static ArrayList<String> COLUMN_NAMES_FOR_UPDATE = new ArrayList<String>(1);

        static {
            COLUMN_NAMES.add("locale");
            COLUMN_NAMES_FOR_UPDATE.add("locale");
        }

        public Row(String locale) {
            setPrimaryKey(new AndroidMetadataTable.PrimaryKey());
            mLocale = locale;
        }

        public void setLocale(String locale) {
            if (((mLocale == null)&&(locale == null))||((mLocale!= null)&&mLocale.equals(locale))) {
                return ;
            }
            mLocale = locale;
            setIsModified(true);
        }

        public String getLocale() {
            return mLocale;
        }

        @Override
        protected Object insert(Transaction transaction) {
            transaction.insert(COLUMN_NAMES, mLocale);
            return transaction.insert(COLUMN_NAMES, mLocale);
        }

        @Override
        protected void update(Transaction transaction) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void remove(Transaction transaction) {
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
