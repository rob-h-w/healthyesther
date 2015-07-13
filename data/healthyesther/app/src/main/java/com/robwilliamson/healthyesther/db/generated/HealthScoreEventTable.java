
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class HealthScoreEventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class HealthScoreEventTablePrimaryKey {

        private long mEventId;
        private long mHealthScoreId;

        public boolean equals(HealthScoreEventTable.HealthScoreEventTablePrimaryKey health_score_eventTablePrimaryKey) {
            if (health_score_eventTablePrimaryKey == null) {
                return false;
            }
            if (health_score_eventTablePrimaryKey == this) {
                return true;
            }
            if (health_score_eventTablePrimaryKey.mHealthScoreId!= mHealthScoreId) {
                return false;
            }
            if (health_score_eventTablePrimaryKey.mEventId!= mEventId) {
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


        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.Row eventTableRow, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.Row health_scoreTableRow, Long healthScoreEventScore) {
        }

        public Row(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey eventTablePrimaryKey, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey health_scoreTablePrimaryKey, Long healthScoreEventScore) {
        }

    }

}
