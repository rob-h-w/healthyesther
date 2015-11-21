package com.robwilliamson.healthyesther.db.integration;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;

import javax.annotation.Nonnull;

public class DateTimeConverter implements DateTime.Converter<org.joda.time.DateTime> {
    static {
        DateTime.register(org.joda.time.DateTime.class, new DateTimeConverter());
    }

    @Nonnull
    public static DateTime now() {
        return DateTime.from(Utils.Time.localNow());
    }

    @Nonnull
    @Override
    public DateTime from(@Nonnull org.joda.time.DateTime fromType) {
        return new DateTime(Utils.Time.toLocalString(fromType));
    }

    @Nonnull
    @Override
    public org.joda.time.DateTime to(@Nonnull Class<org.joda.time.DateTime> type, @Nonnull DateTime dateTime) {
        return Utils.Time.fromLocalString(dateTime.getString());
    }
}
