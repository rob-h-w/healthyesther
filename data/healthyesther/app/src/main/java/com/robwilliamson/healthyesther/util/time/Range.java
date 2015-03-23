package com.robwilliamson.healthyesther.util.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;

public class Range extends TimeRegion {
    public final DateTime from;
    public final DateTime to;
    public final DateTime centre;
    public final Duration sigma;

    public Range(DateTime from, DateTime to) {
        this.from = from.isBefore(to) ? from : to;
        this.to = to.isAfter(from) ? to : from;
        this.sigma = Duration.millis((this.to.getMillis() - this.from.getMillis()) / 2);
        this.centre = this.from.plus(this.sigma);
    }

    public Range(DateTime centre, Duration sigma) {
        this.centre = centre;
        this.sigma = sigma;
        this.from = centre.minus(sigma);
        this.to = centre.plus(sigma);
    }

    public Range starting(DateTime time) {
        return new Range(time, time.plus(sigma).plus(sigma));
    }

    @Override
    public boolean overlaps(TimeRegion region, Comparison comparison) {
        if (contains(region, comparison)) {
            return true;
        }

        if (region.contains(from, comparison) || region.contains(to, comparison)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean contains(TimeRegion region, Comparison comparison) {
        return region.isIn(this, comparison);
    }

    @Override
    public boolean contains(ReadableInstant instant, Comparison comparison) {
        if (instant.isAfter(from) && instant.isBefore(to)) {
            return true;
        }

        if (comparison == Comparison.EXCLUSIVE) {
            return false;
        }

        return instant.isEqual(from) || instant.isEqual(to);
    }

    @Override
    protected boolean isIn(TimeRegion region, Comparison comparison) {
        return region.contains(from, comparison) && region.contains(to, comparison);
    }
}
