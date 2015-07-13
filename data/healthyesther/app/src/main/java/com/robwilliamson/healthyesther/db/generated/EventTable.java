
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class EventTable
    extends Table
{



    /**
     * This class is generated, and should not be edited. Edits will be overwritten
     * 
     */
    public final static class EventTablePrimaryKey {

        private long mId;

        public boolean equals(EventTable.EventTablePrimaryKey eventTablePrimaryKey) {
            if (eventTablePrimaryKey == null) {
                return false;
            }
            if (eventTablePrimaryKey == this) {
                return true;
            }
            if (eventTablePrimaryKey.mId!= mId) {
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


        public Row(String eventCreated, String eventWhen, com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row event_typeTableRow, String eventModified, String eventName, Long eventId) {
        }

        public Row(String eventCreated, String eventWhen, com.robwilliamson.healthyesther.db.generated.EventTypeTable.EventTypeTablePrimaryKey event_typeTablePrimaryKey, String eventModified, String eventName, Long eventId) {
        }

    }

}
