/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.util.Log;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.includes.DateTime;

import java.time.ZonedDateTime;

import javax.annotation.Nonnull;

public class DateTimeConverter implements DateTime.Converter<ZonedDateTime> {
    private static final String LOG_TAG = DateTimeConverter.class.getName();

    static {
        DateTime.register(ZonedDateTime.class, new DateTimeConverter());
    }

    @Nonnull
    public static DateTime now() {
        return DateTime.from(Utils.Time.localNow());
    }

    @Nonnull
    @Override
    public DateTime from(@Nonnull ZonedDateTime fromType) {
        return new DateTime(Utils.Time.toLocalString(fromType));
    }

    @Nonnull
    @Override
    public ZonedDateTime to(@Nonnull Class<ZonedDateTime> type, @Nonnull DateTime dateTime) {
        try {
            return Utils.Time.fromLocalString(dateTime.getString());
        } catch (IllegalArgumentException e) {
            Log.w(LOG_TAG, "Error parsing local string.", e);
        }

        return Utils.Time.fromUtcString(dateTime.getString());
    }
}
