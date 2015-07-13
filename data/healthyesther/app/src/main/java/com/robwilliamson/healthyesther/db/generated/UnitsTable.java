
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class UnitsTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(String unitsName, Double unitsSiFactor, Long unitsId) {
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class UnitsTablePrimaryKey {

        private long mId;

        public boolean equals(UnitsTable.UnitsTablePrimaryKey unitsTablePrimaryKey) {
            if (unitsTablePrimaryKey == null) {
                return false;
            }
            if (unitsTablePrimaryKey == this) {
                return true;
            }
            if (unitsTablePrimaryKey.mId!= mId) {
                return false;
            }
            return true;
        }

    }

}
