/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.db.generated.EventTable;

import org.apache.commons.math3.stat.Frequency;
import org.joda.time.DateTime;

import java.util.List;

import javax.annotation.Nonnull;

public class WeeklyFrequency extends Frequency {
    public WeeklyFrequency(@Nonnull List<EventTable.Row> events) {
        for (EventTable.Row event : events) {
            addValue(name(event));
        }
    }

    @Nonnull
    static String name(@Nonnull EventTable.Row event) {
        DateTime when = event.getWhen().as(DateTime.class);
        return String.valueOf(when.dayOfWeek().get()) + ':' + when.getHourOfDay();
    }
}
