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
            return string.substring(0, 1).toUpperCase();
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
