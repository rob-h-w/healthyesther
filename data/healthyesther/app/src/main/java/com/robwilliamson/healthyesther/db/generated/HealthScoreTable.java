
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class HealthScoreTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class HealthScoreTablePrimaryKey {

        private long mId;

        public boolean equals(HealthScoreTable.HealthScoreTablePrimaryKey health_scoreTablePrimaryKey) {
            if (health_scoreTablePrimaryKey == null) {
                return false;
            }
            if (health_scoreTablePrimaryKey == this) {
                return true;
            }
            if (health_scoreTablePrimaryKey.mId == mId) {
            }
            return true;
        }

    }


    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class Row {


        public Row(long healthScoreBestValue, String healthScoreName, boolean healthScoreRandomQuery, String healthScoreMaxLabel, String healthScoreMinLabel, Long healthScoreId) {
        }

    }

}
