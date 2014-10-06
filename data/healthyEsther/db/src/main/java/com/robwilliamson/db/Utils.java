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
        private static final String sFormat = "yyyy-MM-dd HH:mm:ss";

        public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

        public static void setLocalFieldOnUtc(Calendar utc, int field, int value) {
            Calendar local = ((Calendar)utc.clone());
            local.setTimeZone(TimeZone.getDefault());
            local.set(field, value);
            local.setTimeZone(UTC);
            utc.set(field, local.get(field));
        }

        public static String toString(Calendar calendar) {
            SimpleDateFormat format = new SimpleDateFormat(sFormat);
            return format.format(calendar.getTime());
        }

        public static Calendar fromString(String string) throws ParseException {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("utc"));
            SimpleDateFormat format = new SimpleDateFormat(sFormat, Locale.ROOT);
            calendar.setTime(format.parse(string));
            return calendar;
        }
    }
}
