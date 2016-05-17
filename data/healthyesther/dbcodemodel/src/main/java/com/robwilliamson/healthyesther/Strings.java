/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

public final class Strings {
    public static String checkNotEmpty(String string, String name) {
        if (!name.equals("name")) {
            checkNotEmpty(name, "name");
        }

        if (isEmpty(string)) {
            throw new NullPointerException(String.format("%s is null or empty.", name));
        }

        return string;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String capitalize(String string) {
        if (isEmpty(string)) {
            return "";
        }

        if (string.length() == 1) {
            return string.toUpperCase();
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String lowerCase(String string) {
        if (isEmpty(string)) {
            return "";
        }

        if (string.length() == 1) {
            return string.toLowerCase();
        }

        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    public static String constantName(String string) {
        if (isEmpty(string)) {
            return "";
        }

        StringBuilder builder = new StringBuilder(string.length());

        String separator = "";
        for (char ch : string.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                builder.append(separator);
                builder.append(ch);
            } else {
                builder.append(Character.toUpperCase(ch));
            }

            separator = "_";
        }

        return builder.toString();
    }

    private static String charToCamel(char c, String string) {
        if (isEmpty(string)) {
            return "";
        }

        StringBuilder builder = new StringBuilder(string.length());

        boolean capitalizeNext = false;
        for (char ch : string.toCharArray()) {
            if (ch != c) {
                if (capitalizeNext) {
                    builder.append(Character.toUpperCase(ch));
                    capitalizeNext = false;
                } else {
                    builder.append(ch);
                }
            } else {
                capitalizeNext = true;
            }
        }

        return builder.toString();
    }

    public static String underscoresToCamel(String string) {
        return charToCamel('_', string);
    }

    public static String dotsToCamel(String string) {
        return charToCamel('.', string);
    }

    public static String stripSquareBrackets(String string) {
        string = string.trim();

        while (string.startsWith("[")) {
            string = string.substring(1);
            string = string.trim();
        }

        while (string.endsWith("]")) {
            string = string.substring(0, string.length() - 1);
            string = string.trim();
        }

        return string;
    }
}
