/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.util.time;

import java.time.Instant;
import java.time.ZonedDateTime;

public abstract class TimeRegion {
    public final ZonedDateTime from;
    public final ZonedDateTime to;

    protected TimeRegion(ZonedDateTime from, ZonedDateTime to) {
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

    public boolean contains(ZonedDateTime time) {
        return contains(time, Comparison.INCLUSIVE);
    }

    public boolean contains(ZonedDateTime time, Comparison comparison) {
        return contains(time.toInstant(), comparison);
    }

    public boolean contains(Instant instant) {
        return contains(instant, Comparison.INCLUSIVE);
    }

    public abstract boolean contains(Instant instant, Comparison comparison);

    public abstract TimeRegion startingFrom(int year, int monthOfYear, int dayOfMonth);

    protected abstract boolean isIn(TimeRegion region, Comparison comparison);

    public enum Comparison {
        INCLUSIVE,
        EXCLUSIVE
    }
}
