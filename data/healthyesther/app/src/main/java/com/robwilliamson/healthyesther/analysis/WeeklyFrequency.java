/*
  © Robert Williamson 2014-2016.
  This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.NoteEventTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Key;

import org.apache.commons.math3.stat.Frequency;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WeeklyFrequency extends Frequency {
    WeeklyFrequency() {
    }

    WeeklyFrequency(@Nonnull List<EventTable.Row> events) {
        for (EventTable.Row event : events) {
            addValue(event);
        }
    }

    @Nonnull
    static String name(@Nonnull EventTable.Row event) {
        ZonedDateTime when = event.getWhen().as(ZonedDateTime.class);
        return String.valueOf(
                when.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                + ':' + when.getHour();
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
        private final Map<Key, WeeklyFrequency> mFrequencies = new HashMap<>();

        @Override
        public void consider(@Nonnull EventTable.Row row) {
            Database database = HealthDbHelper.getDatabase();
            row.loadAllRelations(database);

            EventTypeTable.PrimaryKey typeKey = row.getEventTypeRow().getConcretePrimaryKey();
            com.robwilliamson.healthyesther.db.integration.EventTypeTable type =
                    com.robwilliamson.healthyesther.db.integration.EventTypeTable.valueOf(typeKey);
            Key keys[];
            int i = 0;

            switch (type) {
                case MEAL:
                    MealEventTable.Row[] meals = Utils.checkNotNull(row.getMealEventEventId());
                    keys = new MealEventTable.PrimaryKey[meals.length];
                    for (MealEventTable.Row meal : meals) {
                        keys[i] = meal.getConcretePrimaryKey();
                        i++;
                    }
                    break;
                case MEDICATION:
                    MedicationEventTable.Row[] meds = Utils.checkNotNull(row.getMedicationEventEventId());
                    keys = new MedicationEventTable.PrimaryKey[meds.length];
                    for (MedicationEventTable.Row med : meds) {
                        keys[i] = med.getConcretePrimaryKey();
                        i++;
                    }
                    break;
                case HEALTH:
                    HealthScoreEventTable.Row[] scores = Utils.checkNotNull(row.getHealthScoreEventEventId());
                    keys = new HealthScoreEventTable.PrimaryKey[scores.length];
                    for (HealthScoreEventTable.Row score : scores) {
                        keys[i] = score.getConcretePrimaryKey();
                        i++;
                    }
                    break;
                case NOTE:
                    NoteEventTable.Row[] notes = Utils.checkNotNull(row.getNoteEventEventId());
                    keys = new NoteEventTable.PrimaryKey[notes.length];
                    for (NoteEventTable.Row note : notes) {
                        keys[i] = note.getConcretePrimaryKey();
                        i++;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            for (Key key : keys) {
                if (!mFrequencies.containsKey(key)) {
                    mFrequencies.put(key, new WeeklyFrequency());
                }

                mFrequencies.get(key).addValue(row);
            }
        }

        @Nullable
        public WeeklyFrequency getFrequencyFor(MealEventTable.PrimaryKey key) {
            return getFor(key);
        }

        @Nullable
        public WeeklyFrequency getFrequencyFor(MedicationEventTable.PrimaryKey key) {
            return getFor(key);
        }

        @Nullable
        public WeeklyFrequency getFrequencyFor(HealthScoreEventTable.PrimaryKey key) {
            return getFor(key);
        }

        @Nullable
        public WeeklyFrequency getFrequencyFor(NoteEventTable.PrimaryKey key) {
            return getFor(key);
        }

        @Nullable
        private WeeklyFrequency getFor(Key eventDetail) {
            return mFrequencies.get(eventDetail);
        }
    }
}
