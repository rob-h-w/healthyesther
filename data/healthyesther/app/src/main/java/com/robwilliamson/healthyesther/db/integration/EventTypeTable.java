package com.robwilliamson.healthyesther.db.integration;

import javax.annotation.Nonnull;

public enum  EventTypeTable {
    MEAL(1, "Meal", null),
    MEDICATION(2, "Take medication", null),
    HEALTH(3, "Health & mood", null),
    NOTE(4, "Note", null);

    private long mId;
    private @Nonnull String mName;
    private String mIcon;

    EventTypeTable(long id, @Nonnull String name, String icon) {
        mId = id;
        mName = name;
        mIcon = icon;
    }

    public long getId() {
        return mId;
    }

    public @Nonnull String getName() {
        return mName;
    }

    public String getIcon() {
        return mIcon;
    }
}
