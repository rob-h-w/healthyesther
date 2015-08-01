
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

        public EventTypeTablePrimaryKey(long id) {
            mId = id;
        }

        public void setId(long id) {
            mId = id;
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

        private String mIcon;
        private String mName;
        private EventTypeTable.EventTypeTablePrimaryKey mId;

        public Row(String name, EventTypeTable.EventTypeTablePrimaryKey id, String icon) {
        }

        public void setId(EventTypeTable.EventTypeTablePrimaryKey id) {
            mId = id;
        }

        public EventTypeTable.EventTypeTablePrimaryKey getId() {
            return mId;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }

    }

}
