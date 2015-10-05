package com.robwilliamson.healthyesther.db.integration;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;

public class DateTimeConverter implements DateTime.Converter<org.joda.time.DateTime> {
    static {
        DateTime.register(org.joda.time.DateTime.class, new DateTimeConverter());
    }

    @Override
    public DateTime convert(org.joda.time.DateTime fromType) {
        return new DateTime(Utils.Time.toDatabaseString(fromType));
    }

    @Override
    public org.joda.time.DateTime convert(DateTime dateTime) {
        return Utils.Time.fromDatabaseDefaultString(dateTime.getString());
    }
}
