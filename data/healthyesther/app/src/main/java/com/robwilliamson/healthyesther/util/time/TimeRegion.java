/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.util.time;

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public abstract class TimeRegion {
    public final DateTime from;
    public final DateTime to;

    protected TimeRegion(DateTime from, DateTime to) {
        this.from = from.isBefore(to) ? from : to;
        this.to = to.isAfter(from) ? to : from;
    }

    public boolean overlaps(TimeRegion region) {
        return overlaps(region, Comparison.INCLUSIVE);
    }

    public abstract boolean overlaps(TimeRegion region, Comparison comparison);

    public boolean contains(TimeRegion region) {
        return contains(region, Comparison.INCLUSIVE);
    }

    public abstract boolean contains(TimeRegion region, Comparison comparison);

    public boolean contains(ReadableInstant instant) {
        return contains(instant, Comparison.INCLUSIVE);
    }

    public abstract boolean contains(ReadableInstant instant, Comparison comparison);

    public abstract TimeRegion startingFrom(int year, int monthOfYear, int dayOfMonth);

    protected abstract boolean isIn(TimeRegion region, Comparison comparison);

    public enum Comparison {
        INCLUSIVE,
        EXCLUSIVE
    }
}
