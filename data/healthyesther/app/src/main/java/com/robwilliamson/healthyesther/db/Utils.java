package com.robwilliamson.healthyesther.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import com.robwilliamson.healthyesther.db.data.MealData;
import com.robwilliamson.healthyesther.db.definition.MealEvent;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

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

    public static String join(Object[] list, String separator) {
        return join(separator, true, list);
    }

    public static String join(String separator, boolean joinNull, ArrayList<? extends Object> list) {
        return join(separator, joinNull, list.toArray());
    }

    public static String join(String separator, boolean joinNull, Object... list) {
        if (list == null) {
            return "";
        }

        StringBuilder str = new StringBuilder();
        String thisSeparator = "";
        for (Object item : list) {
            if (!joinNull && item == null) {
                continue;
            }

            str.append(thisSeparator).append(item);
            thisSeparator = separator;
        }

        return str.toString();
    }

    public static boolean noString(String string) {
        return string == null || string.length() == 0;
    }

    public static String assertString(String string) {
        if (noString(string)) {
            throw new StringMissingException();
        }

        return string;
    }

    public static boolean equals(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        return !(left == null || right == null) && left.equals(right);
    }

    public static class StringMissingException extends RuntimeException {
    }

    public static class Time {
        private static final String LOG_TAG = Time.class.getSimpleName();
        private static final String TZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss ZZ";
        private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        private static final String DB_DEFAULT_FORMAT = "yyyy-MM-dd' 'HH:mm:ss";
        private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormat.forPattern(TZ_FORMAT).withOffsetParsed();
        private static final DateTimeFormatter DB_FORMATTER = LOCAL_FORMATTER;
        private static final DateTimeFormatter DB_DEFAULT_FORMATTER = DateTimeFormat.forPattern(DB_DEFAULT_FORMAT).withZoneUTC();
        private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormat.forPattern(FORMAT).withZoneUTC();

        public static void bundle(Bundle bundle, String name, DateTime dateTime) {
            bundle.putString(name, toString(dateTime, ISODateTimeFormat.dateTime()));
        }

        public static DateTime unBundle(Bundle bundle, String name) {
            String raw = bundle.getString(name);
            return fromString(raw, ISODateTimeFormat.dateTime());
        }

        public static DateTime localNow() {
            return DateTime.now().withZone(DateTimeZone.forTimeZone(TimeZone.getDefault()));
        }

        public static String toLocalString(DateTime dateTime) {
            return toString(dateTime, LOCAL_FORMATTER);
        }

        public static DateTime fromLocalString(String string) {
            return fromString(string, LOCAL_FORMATTER);
        }

        public static String toUtcString(DateTime dateTime) {
            return toString(dateTime, UTC_FORMATTER);
        }

        public static DateTime fromUtcString(String string) {
            return fromString(string, UTC_FORMATTER);
        }

        public static String toDatabaseString(DateTime dateTime) {
            return toString(dateTime, DB_FORMATTER);
        }

        /**
         * We write to the database using a format with the zone offset, even if this is UTC.
         * The database's default time format does not provide a zone offset, and assumes UTC. For
         * this reason, we must be able to parse 2 string formats when reading DateTimes from the
         * database.
         *
         * @param string The DB String representation of the DateTime.
         * @return The JodaTime DateTime or null if string is null.
         */
        public static DateTime fromDatabaseString(String string) {
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

        public static DateTime fromDatabaseDefaultString(String string) {
            return fromString(string, DB_DEFAULT_FORMATTER);
        }

        public static String toString(DateTime dateTime, DateTimeFormatter formatter) {
            if (dateTime == null) {
                return null;
            }

            return formatter.print(dateTime);
        }

        public static DateTime fromString(String string, DateTimeFormatter formatter) {
            com.robwilliamson.healthyesther.Utils.checkNotNull(string);
            return formatter.parseDateTime(string);
        }

        public static long toBootRealTimeElapsedMillis(DateTime time) {
            long bootOffsetMillis = SystemClock.elapsedRealtime();
            long bootTimeMillis = DateTime.now().getMillis() - bootOffsetMillis;
            return time.getMillis() - bootTimeMillis;
        }
    }

    public static class Strings {
        public static boolean validateLength(String string, int min, int max) {
            return string != null && string.length() > min - 1 && string.length() < max + 1;
        }

        public static boolean nullOrEmpty(String string) {
            return string == null || string.isEmpty();
        }

        public static boolean equals(String first, String second) {
            if (first == second) {
                return true;
            }

            if (first == null || second == null) {
                return false;
            }

            return first.equals(second);
        }
    }

    public static class Db {
        public static HashMap<String, Long> cursorToSuggestionList(Cursor cursor,
                                                                   String suggestionColumnName,
                                                                   String rowIdColumnName) {
            final HashMap<String, Long> suggestionIds = new HashMap<String, Long>(cursor.getCount());
            final int suggestionIndex = cursor.getColumnIndex(suggestionColumnName);
            final int rowIdIndex = cursor.getColumnIndex(rowIdColumnName);

            if (cursor.moveToFirst()) {
                do {
                    suggestionIds.put(cursor.getString(suggestionIndex), cursor.getLong(rowIdIndex));
                } while (cursor.moveToNext());
            }

            return suggestionIds;
        }

        public static class TestData {
            private static final String LOG_TAG = TestData.class.getName();
            private static final int pseudoRandomMax = 1000;
            private static int pseudoRandom;
            private static Pair<Integer, Integer> skipDailyEventRange = new Pair<Integer, Integer>(0, 20);
            private static Pair<Integer, Integer> randomMedicationRange = new Pair<Integer, Integer>(0, 4);
            private static Pair<Integer, Integer> breakfast = new Pair<Integer, Integer>(6, 11);
            private static Pair<Integer, Integer> lunch = new Pair<Integer, Integer>(12, 15);
            private static Pair<Integer, Integer> dinner = new Pair<Integer, Integer>(16, 22);
            private static Transaction transaction;

            public static void transactionWithDb(@Nonnull Runnable runnable) {
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
                Database db = HealthDbHelper.getDatabase();
                try (Transaction transaction = db.getTransaction()) {
                    try {
                        HealthDatabase.drop(transaction);
                    } catch (SQLiteException e) {
                        Log.e(LOG_TAG, "Error while dropping old tables.", e);
                    }
                    HealthDatabase.create(transaction);
                }
            }

            public static void insertFakeData() {
                transactionWithDb(new Runnable() {
                    @Override
                    public void run() {
                        Contract c = Contract.getInstance();

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

                        Set<Integer> thirtyOneDayMonths = new HashSet<Integer>();
                        thirtyOneDayMonths.addAll(Arrays.asList(1,
                                3,
                                5,
                                7,
                                8,
                                10,
                                12));
                        Set<Integer> thirtyDayMonths = new HashSet<Integer>();
                        thirtyDayMonths.addAll(Arrays.asList(4,
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
                                            com.robwilliamson.healthyesther.db.includes.DateTime time = com.robwilliamson.healthyesther.db.includes.DateTime.from(DateTime.now().withDate(year, month, day)
                                                    .withTime(hour, minute(), second(), 0));
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

            private static void insertMedication(int year, int month, int day, int hour, String medication, long medicationId) {
                Contract c = Contract.getInstance();
                com.robwilliamson.healthyesther.db.includes.DateTime time = time(year, month, day, hour);
                EventTable.Row eventRow = new EventTable.Row(EventTypeTable.MEDICATION.getId(),
                        time,
                        time,
                        null,
                        medication);
                eventRow.applyTo(transaction);
                MedicationEventTable.Row medicationEventRow = new MedicationEventTable.Row(eventRow.getNextPrimaryKey(),
                        new MedicationTable.PrimaryKey(medicationId));
                medicationEventRow.applyTo(transaction);
            }

            private static com.robwilliamson.healthyesther.db.includes.DateTime time(int year, int month,
                                                                                     int day, int hour) {
                return com.robwilliamson.healthyesther.db.includes.DateTime.from(DateTime.now().withDate(year, month, day)
                        .withTime(hour, minute(), second(), 0));
            }

            private static boolean haveDailyMedication(int hour) {
                return createTimeRangedEvent(hour, breakfast) ||
                        createTimeRangedEvent(hour, new Pair<Integer, Integer>(0, 23));
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

            private static boolean createTimeRangedEvent(int hour, Pair<Integer, Integer> timeRange) {
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

            public static void insertV3FakeData() {
                final SQLiteDatabase db = HealthDbHelper.getDatabaseWrapper().getSqliteDatabase();
                transactionWithDb(new Runnable() {
                    @Override
                    public void run() {
                        Contract c = Contract.getInstance();

                        final long gruelId = c.MEAL.insert(db, new MealData("gruel"));

                        for (int i = 0; i < 10; i++) {
                            insertMeal(time(2015, 1, rand() % 28, rand() % 24), gruelId);
                        }
                    }

                    private void insertMeal(com.robwilliamson.healthyesther.db.includes.DateTime time, long mealId) {
                        ContentValues eventValues = new ContentValues();
                        String utcTime = Utils.Time.toUtcString(time.as(DateTime.class));
                        eventValues.put(com.robwilliamson.healthyesther.db.definition.Event.CREATED, utcTime);
                        eventValues.put(com.robwilliamson.healthyesther.db.definition.Event.WHEN, utcTime);
                        eventValues.put(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID, MealEvent.EVENT_TYPE_ID);

                        long eventId = db.insert(com.robwilliamson.healthyesther.db.definition.Event.TABLE_NAME, null, eventValues);

                        ContentValues mealEventValues = new ContentValues();
                        mealEventValues.put(MealEvent.MEAL_ID, mealId);
                        mealEventValues.put(MealEvent.EVENT_ID, eventId);

                        db.insert(MealEvent.TABLE_NAME, null, mealEventValues);
                    }
                });
            }
        }
    }

    public static class File {
        public static String join(Object... list) {
            return Utils.join(java.io.File.separator,
                    false,
                    list);
        }

        public static boolean exists(String path) {
            return (new java.io.File(path)).exists();
        }

        public static void mkdirs(String directories) {
            java.io.File dirs = new java.io.File(directories);
            dirs.mkdirs();
        }

        public static void copy(String from, String to) throws IOException {
            InputStream in = new FileInputStream(new java.io.File(from));

            try {
                copy(in, to);
            } finally {
                in.close();
            }
        }

        public static void copy(InputStream from, String to) throws IOException {
            OutputStream out = new FileOutputStream(new java.io.File(to));

            try {

                byte[] buf = new byte[1024];
                int len;
                while ((len = from.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        }

        public static class Dropbox {
            private static final String ANDROID_DATA = "Android/data";
            private static final String COM_DROPBOX_ANDROID_FILES_SCRATCH = "com.dropbox.android/files/scratch";

            public static boolean isDbFileInDropboxAppFolder() {
                return Utils.File.exists(dbFile());
            }

            public static boolean isDropboxPresent() {
                return Utils.File.exists(folder());
            }

            public static String dbFile() {
                return Utils.File.join(
                        folder(),
                        Contract.NAME);
            }

            private static String folder() {
                return Utils.File.join(
                        Environment.getExternalStorageDirectory().getAbsolutePath(),
                        ANDROID_DATA,
                        COM_DROPBOX_ANDROID_FILES_SCRATCH);
            }
        }
    }
}
