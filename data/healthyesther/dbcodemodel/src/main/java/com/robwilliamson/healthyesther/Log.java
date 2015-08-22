package com.robwilliamson.healthyesther;

public final class Log {
    public static boolean debug = true;
    public static boolean verbose = false;

    public static void d(String message, Object... parameters) {
        log(debug, message, parameters);
    }

    public static void v(String message, Object... parameters) {
        log(verbose, message, parameters);
    }

    private static void log(boolean enabled, String message, Object... parameters) {
        if (!verbose) {
            return;
        }

        System.out.printf(message, parameters).println();
    }
}
