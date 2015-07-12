
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

        public boolean equals(MealTable.MealTablePrimaryKey mealTablePrimaryKey) {
            if (mealTablePrimaryKey == null) {
                return false;
            }
            if (mealTablePrimaryKey == this) {
                return true;
            }
            if (mealTablePrimaryKey.mId == mId) {
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(String mealName, Long mealId) {
        }

    }

}
