
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

        public UnitsTablePrimaryKey(UnitsTable.UnitsTablePrimaryKey other) {
            mId = other.mId;
        }

        public UnitsTablePrimaryKey(long Id) {
            mId = Id;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof UnitsTable.UnitsTablePrimaryKey)) {
                return false;
            }
            UnitsTable.UnitsTablePrimaryKey theUnitsTablePrimaryKey = ((UnitsTable.UnitsTablePrimaryKey) other);
            if (theUnitsTablePrimaryKey.mId!= mId) {
                return false;
            }
            return true;
        }

    }

}
