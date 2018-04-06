/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import javax.annotation.Nonnull;

public enum EventTypeTable {
    MEAL(new com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey(1), "Meal", null),
    MEDICATION(new com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey(2), "Take medication", null),
    HEALTH(new com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey(3), "Health & mood", null),
    NOTE(new com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey(4), "Note", null);

    @Nonnull
    private com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey mId;
    @Nonnull
    private String mName;
    private String mIcon;

    EventTypeTable(@Nonnull com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey id, @Nonnull String name, String icon) {
        mId = id;
        mName = name;
        mIcon = icon;
    }

    @Nonnull
    public static EventTypeTable valueOf(long id) {
        return EventTypeTable.values()[(int) id];
    }

    @Nonnull
    public static EventTypeTable valueOf(@Nonnull com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey key) {
        return valueOf(key.getId() - 1);
    }

    public static void populateTable(@Nonnull com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        for (EventTypeTable eventType : EventTypeTable.values()) {
            com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row row =
                    new com.robwilliamson.healthyesther.db.generated.EventTypeTable.Row(
                            eventType.getName(),
                            eventType.getIcon());
            row.applyTo(transaction);
        }
    }

    @Nonnull
    public com.robwilliamson.healthyesther.db.generated.EventTypeTable.PrimaryKey getId() {
        return mId;
    }

    public
    @Nonnull
    String getName() {
        return mName;
    }

    public String getIcon() {
        return mIcon;
    }

    public static class BadEventTypeException extends RuntimeException {
        public BadEventTypeException(EventTypeTable expected, long actual) {
            super("Expected an event of type " + expected.getName() + " (id " + expected.getId().getId() + "), got id " + actual);
        }
    }
}
