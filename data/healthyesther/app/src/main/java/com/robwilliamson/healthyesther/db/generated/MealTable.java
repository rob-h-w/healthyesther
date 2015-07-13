
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


        public Row(String mealName, Long mealId) {
        }

    }

}
