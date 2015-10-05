package com.robwilliamson.healthyesther.db.integration;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;

import javax.annotation.Nonnull;

public class DateTimeConverter implements DateTime.Converter<org.joda.time.DateTime> {
    static {
        DateTime.register(org.joda.time.DateTime.class, new DateTimeConverter());
    }

    @Override
    public
    @Nonnull
    DateTime convert(@Nonnull org.joda.time.DateTime fromType) {
        return new DateTime(Utils.Time.toDatabaseString(fromType));
    }

    @Override
    public
    @Nonnull
    org.joda.time.DateTime convert(@Nonnull DateTime dateTime) {
        return Utils.Time.fromDatabaseDefaultString(dateTime.getString());
    }
}
