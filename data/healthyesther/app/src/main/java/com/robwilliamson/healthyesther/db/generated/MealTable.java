
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MealTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MealTablePrimaryKey {

        private long mId;

        public MealTablePrimaryKey(MealTable.MealTablePrimaryKey other) {
            mId = other.mId;
        }

        public MealTablePrimaryKey(long id) {
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
            if (!(other instanceof MealTable.MealTablePrimaryKey)) {
                return false;
            }
            MealTable.MealTablePrimaryKey theMealTablePrimaryKey = ((MealTable.MealTablePrimaryKey) other);
            if (theMealTablePrimaryKey.mId!= mId) {
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

        private String mName;
        private MealTable.MealTablePrimaryKey mId;

        public Row(String name, MealTable.MealTablePrimaryKey id) {
        }

        public void setId(MealTable.MealTablePrimaryKey id) {
            mId = id;
        }

        public MealTable.MealTablePrimaryKey getId() {
            return mId;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

    }

}
