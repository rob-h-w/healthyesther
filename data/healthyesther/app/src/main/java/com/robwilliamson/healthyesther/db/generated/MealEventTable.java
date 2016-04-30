
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class MealEventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class MealEventTablePrimaryKey {

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mMealId;

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof MealEventTable.MealEventTablePrimaryKey)) {
                return false;
            }
            MealEventTable.MealEventTablePrimaryKey theMealEventTablePrimaryKey = ((MealEventTable.MealEventTablePrimaryKey) other);
            if (theMealEventTablePrimaryKey.mEventId!= mEventId) {
                return false;
            }
            if (theMealEventTablePrimaryKey.mMealId!= mMealId) {
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


        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.MealTable.Row mealTableRow, Double mealEventAmount, com.robwilliamson.healthyesther.db.generated.UnitsTable.Row unitsTableRow) {
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.MealTable.MealTablePrimaryKey mealTablePrimaryKey, Double mealEventAmount, com.robwilliamson.healthyesther.db.generated.UnitsTable.UnitsTablePrimaryKey unitsTablePrimaryKey) {
        }

    }

}
