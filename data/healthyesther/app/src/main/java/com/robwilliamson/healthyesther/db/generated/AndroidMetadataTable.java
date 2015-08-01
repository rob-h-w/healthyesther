
package com.robwilliamson.healthyesther.db.generated;



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
    public final static class AndroidMetadataTablePrimaryKey {


        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof AndroidMetadataTable.AndroidMetadataTablePrimaryKey)) {
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

        private String mLocale;

        public Row(String locale) {
        }

        public void setLocale(String locale) {
            mLocale = locale;
        }

        public String getLocale() {
            return mLocale;
        }

    }

}
