/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
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
        if (!verbose || !enabled) {
            return;
        }

        System.out.printf(message, parameters).println();
    }
}
