
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

        private long mEventId;
        private long mMealId;

        public boolean equals(MealEventTable.MealEventTablePrimaryKey meal_eventTablePrimaryKey) {
            if (meal_eventTablePrimaryKey == null) {
                return false;
            }
            if (meal_eventTablePrimaryKey == this) {
                return true;
            }
            if (meal_eventTablePrimaryKey.mEventId!= mEventId) {
                return false;
            }
            if (meal_eventTablePrimaryKey.mMealId!= mMealId) {
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
