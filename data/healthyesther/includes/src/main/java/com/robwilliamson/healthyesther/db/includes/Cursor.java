package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public interface Cursor {
    Boolean getBoolean(@Nonnull String column);

    Double getDouble(@Nonnull String column);

    Long getLong(@Nonnull String column);

    String getString(@Nonnull String column);

    DateTime getDateTime(@Nonnull String column);

    void moveToFirst();

    boolean moveToNext();

    int count();
}
