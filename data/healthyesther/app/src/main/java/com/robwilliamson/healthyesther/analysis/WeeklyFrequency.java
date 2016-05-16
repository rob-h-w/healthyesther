/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;

import org.apache.commons.math3.stat.Frequency;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WeeklyFrequency extends Frequency {
    public WeeklyFrequency() {
    }

    public WeeklyFrequency(@Nonnull List<EventTable.Row> events) {
        for (EventTable.Row event : events) {
            addValue(event);
        }
    }

    @Nonnull
    static String name(@Nonnull EventTable.Row event) {
        DateTime when = event.getWhen().as(DateTime.class);
        return String.valueOf(when.dayOfWeek().get()) + ':' + when.getHourOfDay();
    }

    @Nonnull
    static WeeklyFrequency from(@Nonnull EventTable.PrimaryKey[] keys) {
        List<EventTable.Row> events = new ArrayList<>(keys.length);

        for (EventTable.PrimaryKey key : keys) {
            events.add(HealthDatabase.EVENT_TABLE.select1(HealthDbHelper.getDatabase(), key));
        }

        return new WeeklyFrequency(events);
    }

    public void addValue(EventTable.Row event) {
        addValue(name(event));
    }

    static public class Analyzer implements EventAnalyzer {
        private final Map<EventTypeTable.PrimaryKey, WeeklyFrequency> mFrequencies = new HashMap<>();

        @Override
        public void consider(@Nonnull EventTable.Row row) {
            row.loadRelations(HealthDbHelper.getDatabase());

            EventTypeTable.PrimaryKey typeKey = row.getEventTypeRow().getConcretePrimaryKey();
            if (!mFrequencies.containsKey(typeKey)) {
                mFrequencies.put(typeKey, new WeeklyFrequency());
            }

            mFrequencies.get(typeKey).addValue(row);
        }

        @Nullable
        public WeeklyFrequency getFrequencyFor(EventTypeTable.PrimaryKey eventType) {
            return mFrequencies.get(eventType);
        }
    }
}
