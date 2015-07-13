
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class EventTypeTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class EventTypeTablePrimaryKey {

        private long mId;

        public boolean equals(EventTypeTable.EventTypeTablePrimaryKey event_typeTablePrimaryKey) {
            if (event_typeTablePrimaryKey == null) {
                return false;
            }
            if (event_typeTablePrimaryKey == this) {
                return true;
            }
            if (event_typeTablePrimaryKey.mId!= mId) {
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


        public Row(String eventTypeName, String eventTypeIcon, Long eventTypeId) {
        }

    }

}
