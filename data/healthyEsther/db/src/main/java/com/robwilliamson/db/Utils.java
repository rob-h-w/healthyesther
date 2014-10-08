package com.robwilliamson.db;

import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;

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
        private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
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
}
