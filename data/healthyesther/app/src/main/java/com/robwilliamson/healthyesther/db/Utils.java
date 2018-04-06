/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

public final class Utils {
    private Utils() {
    }

    public static void nonnull(Object... values) {
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];

            if (value == null) {
                throw new NullPointerException("Value at index " + i + " was null.");
            }
        }
    }

    public static boolean noString(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean equals(Object left, Object right) {
        return left == null
                && right == null
                || !(left == null || right == null)
                && left.equals(right);

    }

    public static class Time {
        @SuppressWarnings("WeakerAccess")
        public final static long SECOND_MS = 1000;
        @SuppressWarnings("WeakerAccess")
        public final static long MINUTE_MS = 60 * SECOND_MS;
        public final static long HOUR_MS = 60 * MINUTE_MS;
        public final static long DAY_MS = 24 * HOUR_MS;
        private static final String LOG_TAG = Time.class.getSimpleName();
        private static final String TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss xxx";
        private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        private static final String DB_DEFAULT_FORMAT = "yyyy-MM-dd' 'HH:mm:ss";
        private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ofPattern(TZ_FORMAT);
        private static final DateTimeFormatter DB_FORMATTER = LOCAL_FORMATTER;
        private static final DateTimeFormatter DB_DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DB_DEFAULT_FORMAT)
                .withZone(ZoneId.systemDefault());
        private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter
                .ofPattern(FORMAT)
                .withZone(ZoneOffset.UTC);

        public static void bundle(Bundle bundle, String name, ZonedDateTime dateTime) {
            bundle.putString(name, toString(dateTime, LOCAL_FORMATTER));
        }

        public static ZonedDateTime unBundle(Bundle bundle, String name) {
            String raw = bundle.getString(name);
            return fromString(raw, LOCAL_FORMATTER);
        }

        public static ZonedDateTime localNow() {
            return ZonedDateTime.now();
        }

        public static String toLocalString(ZonedDateTime dateTime) {
            return toString(dateTime, LOCAL_FORMATTER);
        }

        public static ZonedDateTime fromLocalString(String string) {
            return fromString(string, LOCAL_FORMATTER);
        }

        public static String toUtcString(ZonedDateTime dateTime) {
            return toString(dateTime, UTC_FORMATTER);
        }

        public static ZonedDateTime fromUtcString(String string) {
            return fromString(string, UTC_FORMATTER);
        }

        public static String toDatabaseString(ZonedDateTime dateTime) {
            return toString(dateTime, DB_FORMATTER);
        }

        /**
         * We write to the database using a format with the zone offset, even if this is UTC.
         * The database's default time format does not provide a zone offset, and assumes UTC. For
         * this reason, we must be able to parse 2 string formats when reading DateTimes from the
         * database.
         *
         * @param string The DB String representation of the DateTime.
         * @return The ZonedDateTime or null if string is null.
         */
        public static ZonedDateTime fromDatabaseString(String string) {
            if (string == null) {
                return null;
            }

            try {
                return fromString(string, DB_FORMATTER);
            } catch (IllegalArgumentException e) {
                Log.w(LOG_TAG, e);
                return fromDatabaseDefaultString(string);
            }
        }

        static ZonedDateTime fromDatabaseDefaultString(String string) {
            return fromString(string, DB_DEFAULT_FORMATTER);
        }

        public static String toString(ZonedDateTime dateTime, DateTimeFormatter formatter) {
            if (dateTime == null) {
                return null;
            }

            return formatter.format(dateTime);
        }

        static ZonedDateTime fromString(String string, DateTimeFormatter formatter) {
            com.robwilliamson.healthyesther.Utils.checkNotNull(string);
            return ZonedDateTime.parse(string, formatter);
        }

        public static long toBootRealTimeElapsedMillis(ZonedDateTime time) {
            long bootOffsetMillis = SystemClock.elapsedRealtime();
            long bootTimeMillis = ZonedDateTime.now().toInstant().toEpochMilli() - bootOffsetMillis;
            return time.toInstant().toEpochMilli() - bootTimeMillis;
        }
    }

    public static class Strings {

        public static boolean nullOrEmpty(String string) {
            return string == null || string.isEmpty();
        }

        public static boolean equals(String first, String second) {
            return first.equals(second) || second != null && first.equals(second);

        }
    }

    public static class Db {

        @Nonnull
        public static String cleanName(@Nonnull String name) {
            // Strip square brackets, if present.
            if (name.startsWith("[")) {
                name = name.substring(1);
            }

            if (name.endsWith("]")) {
                name = name.substring(0, name.length() - 1);
            }

            return name;
        }

        public static class TestData {
            @SuppressWarnings("unused")
            private static final String LOG_TAG = TestData.class.getName();
            private static final int pseudoRandomMax = 1000;
            private static int pseudoRandom;
            private static Pair<Integer, Integer> skipDailyEventRange = new Pair<>(0, 20);
            private static Pair<Integer, Integer> randomMedicationRange = new Pair<>(0, 4);
            private static Pair<Integer, Integer> breakfast = new Pair<>(6, 11);
            private static Pair<Integer, Integer> lunch = new Pair<>(12, 15);
            private static Pair<Integer, Integer> dinner = new Pair<>(16, 22);
            private static Transaction transaction;

            static void transactionWithDb(@Nonnull Runnable runnable) {
                Database database = HealthDbHelper.getDatabase();

                try (Transaction t = database.getTransaction()) {
                    transaction = t;
                    runnable.run();
                    transaction.commit();
                } finally {
                    transaction = null;
                }
            }

            public static void cleanOldData() {
                HealthDbHelper.closeDb();

                Database db = HealthDbHelper.getDatabase();

                try (Transaction transaction = db.getTransaction()) {
                    DatabaseAccessor.drop(transaction);
                    DatabaseAccessor.create(transaction);
                    transaction.commit();
                }

                HealthDbHelper.closeDb();
            }

            public static void insertFakeData() {
                transactionWithDb(() -> {
                    long[] breakfast = new long[]{
                            insertMeal("Toppas"),
                            insertMeal("Toast"),
                            insertMeal("Yoghurt"),
                            insertMeal("Melon")
                    };

                    long[] lunch = new long[]{
                            insertMeal("Sandwiches"),
                            insertMeal("Soup")
                    };

                    long[] dinner = new long[]{
                            insertMeal("Coq au vin"),
                            insertMeal("Spaghetti bolognese"),
                            insertMeal("Beef and prune casserole"),
                            insertMeal("Frauen pizza"),
                            insertMeal("Chester Burger"),
                            insertMeal("Männer pizza")
                    };

                    long[] medication = new long[]{
                            insertMedication("Paracetamol"),
                            insertMedication("Ibuprofen"),
                            insertMedication("ACC Akut"),
                            insertMedication("Fluoxetine"),
                            insertMedication("Citalopram"),
                            insertMedication("Escitalopram")
                    };

                    long dailyMedicationId = medication[5];

                    Set<Integer> thirtyOneDayMonths = new HashSet<>(Arrays.asList(1,
                            3,
                            5,
                            7,
                            8,
                            10,
                            12));
                    Set<Integer> thirtyDayMonths = new HashSet<>(Arrays.asList(4,
                            6,
                            9,
                            11));

                    for (int year = 2013; year <= 2014; year++) {
                        for (int month = 1; month <= 12; month++) {
                            final int daysInMonth = thirtyDayMonths.contains(month) ? 30 :
                                    (thirtyOneDayMonths.contains(month) ? 31 : 28);
                            for (int day = 1; day <= daysInMonth; day++) {
                                boolean hadDailyMedication = false;
                                boolean hadBreakfast = false;
                                boolean hadLunch = false;
                                boolean hadDinner = false;
                                for (int hour = 0; hour < 24; hour++) {
                                    pseudoRandom = pseudoRandom + 31 * hour * day * month * year;
                                    pseudoRandom |= pseudoRandom << 17;
                                    pseudoRandom |= pseudoRandom >> 23;
                                    pseudoRandom %= pseudoRandomMax;

                                    if (!hadDailyMedication && haveDailyMedication(hour)) {
                                        com.robwilliamson.healthyesther.db.includes.DateTime time =
                                                com.robwilliamson.healthyesther.db.includes
                                                    .DateTime.from(ZonedDateTime.now()
                                                        .withYear(year)
                                                        .withMonth(month)
                                                        .withDayOfMonth(day)
                                                        .withHour(hour)
                                                        .withMinute(minute())
                                                        .withSecond(second())
                                                        .withNano(0));
                                        EventTable.Row eventRow = new EventTable.Row(
                                                EventTypeTable.MEDICATION.getId(),
                                                time,
                                                time,
                                                null,
                                                "Daily medication"
                                        );
                                        eventRow.applyTo(transaction);
                                        MedicationEventTable.Row medicationEvent = new MedicationEventTable.Row(eventRow.getNextPrimaryKey(),
                                                new MedicationTable.PrimaryKey(dailyMedicationId));
                                        medicationEvent.applyTo(transaction);
                                        hadDailyMedication = true;
                                    }

                                    if (!hadBreakfast && haveBreakfast(hour)) {
                                        insertMeal(
                                                year,
                                                month,
                                                day,
                                                hour,
                                                "Breakfast",
                                                randomId(breakfast));
                                        hadBreakfast = true;
                                    }

                                    if (!hadLunch && haveLunch(hour)) {
                                        insertMeal(
                                                year, month, day, hour,
                                                "Lunch",
                                                randomId(lunch));
                                        hadLunch = true;
                                    }

                                    if (!hadDinner && haveDinner(hour)) {
                                        insertMeal(
                                                year, month, day, hour,
                                                "Dinner",
                                                randomId(dinner));
                                        hadDinner = true;
                                    }

                                    while (inRange(rand(), randomMedicationRange)) {
                                        insertMedication(
                                                year, month, day, hour,
                                                "Medication",
                                                randomId(medication));
                                    }
                                }
                            }
                        }
                    }
                });
            }

            private static long insertMeal(@Nonnull String name) {
                MealTable.Row row = new MealTable.Row(name);
                row.applyTo(transaction);
                return row.getNextPrimaryKey().getId();
            }

            private static long insertMedication(@Nonnull String name) {
                MedicationTable.Row row = new MedicationTable.Row(name);
                row.applyTo(transaction);
                return row.getNextPrimaryKey().getId();
            }

            private static long randomId(long[] ids) {
                int idIndex = rand() % ids.length;
                return ids[idIndex];
            }

            private static void insertMeal(int year, int month,
                                           int day, int hour, String name, long mealId) {
                com.robwilliamson.healthyesther.db.includes.DateTime time = time(year, month, day, hour);
                EventTable.Row eventRow = new EventTable.Row(EventTypeTable.MEAL.getId(),
                        time,
                        time,
                        null,
                        name);
                eventRow.applyTo(transaction);
                MealEventTable.Row mealEventRow = new MealEventTable.Row(eventRow.getNextPrimaryKey(),
                        new MealTable.PrimaryKey(mealId),
                        null,
                        null);
                mealEventRow.applyTo(transaction);
            }

            private static void insertMedication(
                    int year,
                    int month,
                    int day,
                    int hour,
                    @SuppressWarnings("SameParameterValue") String medication,
                    long medicationId) {
                com.robwilliamson.healthyesther.db.includes.DateTime time =
                        time(year, month, day, hour);
                EventTable.Row eventRow = new EventTable.Row(EventTypeTable.MEDICATION.getId(),
                        time,
                        time,
                        null,
                        medication);
                eventRow.applyTo(transaction);
                MedicationEventTable.Row medicationEventRow =
                        new MedicationEventTable.Row(eventRow.getNextPrimaryKey(),
                                new MedicationTable.PrimaryKey(medicationId));
                medicationEventRow.applyTo(transaction);
            }

            private static com.robwilliamson.healthyesther.db.includes.DateTime time(
                    int year,
                    int month,
                    int day,
                    int hour) {
                return com.robwilliamson.healthyesther.db.includes.DateTime.from(ZonedDateTime.now()
                        .withYear(year).withMonth(month).withDayOfMonth(day)
                        .withHour(hour).withMinute(minute()).withSecond(second()).withNano(0));
            }

            private static boolean haveDailyMedication(int hour) {
                return createTimeRangedEvent(hour, breakfast) ||
                        createTimeRangedEvent(hour, new Pair<>(0, 23));
            }

            private static boolean haveBreakfast(int hour) {
                return createTimeRangedEvent(hour, breakfast);
            }

            private static boolean haveLunch(int hour) {
                return createTimeRangedEvent(hour, lunch);
            }

            private static boolean haveDinner(int hour) {
                return createTimeRangedEvent(hour, dinner);
            }

            private static boolean createTimeRangedEvent(
                    int hour,
                    Pair<Integer, Integer> timeRange) {
                if (!inRange(hour, timeRange)) {
                    return false;
                }

                int rangeMagnitude = timeRange.second - timeRange.first;
                int rangeOffset = hour - timeRange.first;
                int threshold = rangeOffset * pseudoRandomMax / rangeMagnitude;

                return threshold >= rand() && !inRange(rand(), skipDailyEventRange);
            }

            private static boolean inRange(int value, Pair<Integer, Integer> range) {
                return !(value < range.first || value > range.second);
            }

            private static int second() {
                return minute();
            }

            private static int minute() {
                return rand() % 60;
            }

            private static int rand() {
                pseudoRandom |= pseudoRandom << 11;
                pseudoRandom |= pseudoRandom >> 13;
                pseudoRandom += 31;
                pseudoRandom *= 43;
                pseudoRandom %= pseudoRandomMax;
                return pseudoRandom;
            }

            @SuppressWarnings("unused")
            public static void insertV3FakeData() {
                final SQLiteDatabase db = HealthDbHelper.getDatabaseWrapper().getSqliteDatabase();
                transactionWithDb(new Runnable() {
                    @Override
                    public void run() {
                        MealTable.Row row = new MealTable.Row("gruel");
                        row.applyTo(transaction);

                        for (int i = 0; i < 10; i++) {
                            insertMeal(time(2015, 1, rand() % 28, rand() % 24), row.getNextPrimaryKey());
                        }
                    }

                    private void insertMeal(
                            @Nonnull com.robwilliamson.healthyesther.db.includes.DateTime time,
                            @Nonnull MealTable.PrimaryKey primaryKey) {
                        ContentValues eventValues = new ContentValues();
                        String utcTime = Utils.Time.toUtcString(time.as(ZonedDateTime.class));
                        eventValues.put(EventTable.CREATED, utcTime);
                        eventValues.put('[' + EventTable.WHEN + ']', utcTime);
                        eventValues.put(EventTable.TYPE_ID, EventTypeTable.MEAL.getId().getId());

                        long eventId = db.insert(DatabaseAccessor.EVENT_TABLE.getName(), null, eventValues);

                        ContentValues mealEventValues = new ContentValues();
                        mealEventValues.put(MealEventTable.MEAL_ID, primaryKey.getId());
                        mealEventValues.put(MealEventTable.EVENT_ID, eventId);

                        db.insert(DatabaseAccessor.MEAL_EVENT_TABLE.getName(), null, mealEventValues);
                    }
                });
            }
        }
    }

    public static class File {

        public static boolean exists(String path) {
            return (new java.io.File(path)).exists();
        }

        public static void copy(InputStream from, String to) throws IOException {

            try (OutputStream out = new FileOutputStream(new java.io.File(to))) {

                byte[] buf = new byte[1024];
                int len;
                while ((len = from.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
}
