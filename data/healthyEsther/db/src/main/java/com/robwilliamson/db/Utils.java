package com.robwilliamson.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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

        public static class BadTimeStringFormatException extends RuntimeException {
            BadTimeStringFormatException(String badString, ParseException e) {
                super("Could not parse " + badString, e);
            }
        }

        public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

        public static Calendar getLocalClone(Calendar calendar) {
            Calendar local = ((Calendar)calendar.clone());
            local.setTimeZone(TimeZone.getDefault());
            return local;
        }

        public static void setLocalFieldOnUtc(Calendar utc, int field, int value) {
            Calendar local = getLocalClone(utc);
            local.set(field, value);
            local.setTimeZone(UTC);
            utc.set(field, local.get(field));
        }

        public static int getLocalFieldFromUtc(Calendar utc, int field) {
            Calendar local = getLocalClone(utc);
            return local.get(field);
        }

        public static String toString(Calendar calendar) {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT);
            return format.format(calendar.getTime());
        }

        public static Calendar fromString(String string) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("utc"));
            SimpleDateFormat format = new SimpleDateFormat(FORMAT, Locale.ROOT);
            try {
                calendar.setTime(format.parse(string));
            } catch (ParseException e) {
                throw new BadTimeStringFormatException(string, e);
            }
            return calendar;
        }
    }
}
