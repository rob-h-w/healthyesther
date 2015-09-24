
package com.robwilliamson.healthyesther.db.generated;

import java.util.ArrayList;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class AndroidMetadataTable
    extends Table
{



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

        public final static ArrayList<String> COLUMN_NAMES = new ArrayList<String>(1);
        private String mLocale;

        static {
            COLUMN_NAMES.add("locale");
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
        public Object insert(Transaction transaction) {
            transaction.insert(COLUMN_NAMES, mLocale);
            return transaction.insert(COLUMN_NAMES, mLocale);
        }

        @Override
        public void update(Transaction transaction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(Transaction transaction) {
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
            return true;
        }

    }

}
