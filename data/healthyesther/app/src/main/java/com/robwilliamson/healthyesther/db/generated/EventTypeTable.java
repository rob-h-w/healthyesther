
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

        public EventTypeTablePrimaryKey(EventTypeTable.EventTypeTablePrimaryKey other) {
            mId = other.mId;
        }

        public EventTypeTablePrimaryKey(long Id) {
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
            if (!(other instanceof EventTypeTable.EventTypeTablePrimaryKey)) {
                return false;
            }
            EventTypeTable.EventTypeTablePrimaryKey theEventTypeTablePrimaryKey = ((EventTypeTable.EventTypeTablePrimaryKey) other);
            if (theEventTypeTablePrimaryKey.mId!= mId) {
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
