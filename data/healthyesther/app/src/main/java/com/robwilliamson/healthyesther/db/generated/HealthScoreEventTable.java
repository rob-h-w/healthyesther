
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

        private com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey mEventId;
        private com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey mHealthScoreId;

        public HealthScoreEventTablePrimaryKey(HealthScoreEventTable.HealthScoreEventTablePrimaryKey other) {
            mEventId = other.mEventId;
            mHealthScoreId = other.mHealthScoreId;
        }

        public HealthScoreEventTablePrimaryKey(com.robwilliamson.healthyesther.db.generated.EventTable.EventTablePrimaryKey EventId, com.robwilliamson.healthyesther.db.generated.HealthScoreTable.HealthScoreTablePrimaryKey HealthScoreId) {
            mEventId = EventId;
            mHealthScoreId = HealthScoreId;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (!(other instanceof HealthScoreEventTable.HealthScoreEventTablePrimaryKey)) {
                return false;
            }
            HealthScoreEventTable.HealthScoreEventTablePrimaryKey theHealthScoreEventTablePrimaryKey = ((HealthScoreEventTable.HealthScoreEventTablePrimaryKey) other);
            if (theHealthScoreEventTablePrimaryKey.mEventId!= mEventId) {
                if ((theHealthScoreEventTablePrimaryKey.mEventId == null)||(!theHealthScoreEventTablePrimaryKey.mEventId.equals(mEventId))) {
                    return false;
                }
            }
            if (theHealthScoreEventTablePrimaryKey.mHealthScoreId!= mHealthScoreId) {
                if ((theHealthScoreEventTablePrimaryKey.mHealthScoreId == null)||(!theHealthScoreEventTablePrimaryKey.mHealthScoreId.equals(mHealthScoreId))) {
                    return false;
                }
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
