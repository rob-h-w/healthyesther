
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

        private double mSiFactor;
        private String mName;
        private UnitsTable.UnitsTablePrimaryKey mId;

        public Row(String name, UnitsTable.UnitsTablePrimaryKey id, Double siFactor) {
        }

        public void setId(UnitsTable.UnitsTablePrimaryKey id) {
            mId = id;
        }

        public UnitsTable.UnitsTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public void setSiFactor(double siFactor) {
            mSiFactor = siFactor;
        }

        public double getSiFactor() {
            return mSiFactor;
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

        public UnitsTablePrimaryKey(long id) {
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
