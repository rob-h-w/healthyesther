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
}
