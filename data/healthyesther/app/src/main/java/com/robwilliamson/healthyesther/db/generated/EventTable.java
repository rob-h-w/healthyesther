
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

        public EventTablePrimaryKey(EventTable.EventTablePrimaryKey other) {
            mId = other.mId;
        }

        public EventTablePrimaryKey(long Id) {
            mId = Id;
        }

        public void setId(long Id) {
            mId = Id;
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
            if (!(other instanceof EventTable.EventTablePrimaryKey)) {
                return false;
            }
            EventTable.EventTablePrimaryKey theEventTablePrimaryKey = ((EventTable.EventTablePrimaryKey) other);
            if (theEventTablePrimaryKey.mId!= mId) {
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
