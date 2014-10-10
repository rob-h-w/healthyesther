package com.robwilliamson.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Utils {
    private Utils() {}

    public static class StringMissingException extends RuntimeException {}

    public static String join(ArrayList<? extends Object> list, String separator) {
        return join(list.toArray(), separator);
    }

    public static String join(Object[] list, String separator) {
        StringBuilder str = new StringBuilder();
        String thisSeparator = "";
        for (Object item : list) {
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

    public static class Time {
        private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        private static final DateTimeFormatter DB_FORMATTER = DateTimeFormat.forPattern(FORMAT).withZoneUTC();

        public static void bundle(Bundle bundle, String name, DateTime dateTime) {
            bundle.putString(name, toString(dateTime, ISODateTimeFormat.dateTime()));
        }

        public static DateTime unBundle(Bundle bundle, String name) {
            String raw = bundle.getString(name);
            return fromString(raw, ISODateTimeFormat.dateTime());
        }

        public static String toDatabaseString(DateTime dateTime) {
            return toString(dateTime, DB_FORMATTER);
        }

        public static DateTime dateTimeFromDatabaseString(String string) {
            return fromString(string, DB_FORMATTER);
        }

        public static String toLocallyFormattedString(DateTime dateTime, String format) {
            DateTime local = dateTime.withZone(DateTimeZone.getDefault());
            return toString(local, DateTimeFormat.forPattern(format));
        }

        public static String toString(DateTime dateTime, DateTimeFormatter formatter) {
            return formatter.print(dateTime);
        }

        public static DateTime fromString(String string, DateTimeFormatter formatter) {
            return formatter.parseDateTime(string);
        }
    }

    public static class Strings {
        public static boolean validateLength(String string, int min, int max) {
            return string != null && string.length() > min - 1 && string.length() < max + 1;
        }

        public static boolean nullOrEmpty(String string) {
            return string == null || string.isEmpty();
        }
    }

    public static class Db {
        public static class TestData {
            private static final int pseudoRandomMax = 1000;
            private static int pseudoRandom;
            private static Pair<Integer, Integer> skipDailyEventRange = new Pair<Integer, Integer>(0, 20);
            private static Pair<Integer, Integer> randomMedicationRange = new Pair<Integer, Integer>(0, 4);
            private static Pair<Integer, Integer> breakfast = new Pair<Integer, Integer>(6, 11);
            private static Pair<Integer, Integer> lunch = new Pair<Integer, Integer>(12, 15);
            private static Pair<Integer, Integer> dinner = new Pair<Integer, Integer>(16, 22);

            public static void insertFakeData(SQLiteDatabase db) {
                Contract c = Contract.getInstance();

                long[] breakfast = new long[] {
                        c.MEAL.insert(db, "Toppas"),
                        c.MEAL.insert(db, "Toast"),
                        c.MEAL.insert(db, "Yoghurt"),
                        c.MEAL.insert(db, "Melon")
                };

                long[] lunch = new long[] {
                        c.MEAL.insert(db, "Sandwiches"),
                        c.MEAL.insert(db, "Soup")
                };

                long[] dinner = new long[] {
                        c.MEAL.insert(db, "Coq au vin"),
                        c.MEAL.insert(db, "Spaghetti bolognese"),
                        c.MEAL.insert(db, "Beef and prune casserole"),
                        c.MEAL.insert(db, "Frauen pizza"),
                        c.MEAL.insert(db, "Chester Burger")
                };

                long[] medication = new long[] {
                        c.MEDICATION.insert(db, "Paracetamol"),
                        c.MEDICATION.insert(db, "Ibuprofen"),
                        c.MEDICATION.insert(db, "ACC Akut"),
                        c.MEDICATION.insert(db, "Fluoxetine")
                };

                long dailyMedicationId = medication[3];

                Set<Integer> thirtyOneDayMonths = new HashSet<Integer>();
                thirtyOneDayMonths.addAll(Arrays.asList(new Integer[] {
                        1,
                        3,
                        5,
                        7,
                        8,
                        10,
                        12
                }));
                Set<Integer> thirtyDayMonths = new HashSet<Integer>();
                thirtyDayMonths.addAll(Arrays.asList(new Integer[]{
                        4,
                        6,
                        9,
                        11
                }));

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
                                pseudoRandom = pseudoRandom + 31*hour*day*month*year;
                                pseudoRandom |= pseudoRandom << 17;
                                pseudoRandom |= pseudoRandom >> 23;
                                pseudoRandom %= pseudoRandomMax;

                                if (!hadDailyMedication && haveDailyMedication(hour)) {
                                    DateTime t = DateTime.now().withDate(year, month, day)
                                            .withTime(hour, minute(), second(), 0);
                                    long eventId = c.EVENT.insert(db, time(
                                            year, month, day, hour), 2, "Daily medication");
                                    c.MEDICATION_EVENT.insert(db, dailyMedicationId, eventId);
                                    hadDailyMedication = true;
                                }

                                if (!hadBreakfast && haveBreakfast(hour)) {
                                    insertMeal(db,
                                            year,
                                            month,
                                            day,
                                            hour,
                                            "Breakfast",
                                            randomId(breakfast));
                                    hadBreakfast = true;
                                }

                                if (!hadLunch && haveLunch(hour)) {
                                    insertMeal(db,
                                            year, month, day, hour,
                                            "Lunch",
                                            randomId(lunch));
                                    hadLunch = true;
                                }

                                if (!hadDinner && haveDinner(hour)) {
                                    insertMeal(db,
                                            year, month, day, hour,
                                            "Dinner",
                                            randomId(dinner));
                                    hadDinner = true;
                                }

                                while (inRange(rand(), randomMedicationRange)) {
                                    insertMedication(db,
                                            year, month, day, hour,
                                            "Medication",
                                            randomId(medication));
                                }
                            }
                        }
                    }
                }
            }

            private static long randomId(long[] ids) {
                int idIndex = rand() % ids.length;
                return ids[idIndex];
            }

            private static void insertMeal(SQLiteDatabase db, int year, int month,
                                           int day, int hour, String name, long mealId) {
                Contract c = Contract.getInstance();
                long eventId = c.EVENT.insert(db, time(
                        year, month, day, hour), 1, name);
                c.MEAL_EVENT.insert(db, mealId, eventId);
            }

            private static void insertMedication(SQLiteDatabase db, int year, int month, int day, int hour, String medication, long medicationId) {
                Contract c = Contract.getInstance();
                long eventId = c.EVENT.insert(db, time(
                        year, month, day, hour), 2, "Medication");
                c.MEDICATION_EVENT.insert(db, medicationId, eventId);
            }

            private static DateTime time(int year, int month,
                                         int day, int hour) {
                return DateTime.now().withDate(year, month, day)
                        .withTime(hour, minute(), second(), 0);
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
        }

        public static HashMap<String, Long> cursorToSuggestionList(Cursor cursor,
                                                                   String suggestionColumnName,
                                                                   String rowIdColumnName) {
            final HashMap<String, Long> suggestionIds = new HashMap<String, Long>(cursor.getCount());
            final int suggestionIndex = cursor.getColumnIndex(suggestionColumnName);
            final int rowIdIndex = cursor.getColumnIndex(rowIdColumnName);

            if (cursor.moveToFirst()) {
                do {
                    suggestionIds.put(cursor.getString(suggestionIndex), cursor.getLong(rowIdIndex));
                } while(cursor.moveToNext());
            }

            return suggestionIds;
        }
    }
}
