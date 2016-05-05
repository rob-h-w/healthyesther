/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.util.Log;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;

import javax.annotation.Nonnull;

public class DateTimeConverter implements DateTime.Converter<org.joda.time.DateTime> {
    private static final String LOG_TAG = DateTimeConverter.class.getName();

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
        try {
            return Utils.Time.fromLocalString(dateTime.getString());
        } catch (IllegalArgumentException e) {
            Log.w(LOG_TAG, "Error parsing local string.", e);
        }

        return Utils.Time.fromUtcString(dateTime.getString());
    }
}
