package com.robwilliamson.healthyesther.util.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;

public class Range extends TimeRegion {
    public final DateTime centre;
    public final Duration sigma;

    public Range(DateTime from, DateTime to) {
        super(from, to);
        this.sigma = Duration.millis((this.to.getMillis() - this.from.getMillis()) / 2);
        this.centre = this.from.plus(this.sigma);
    }

    public Range(DateTime centre, Duration sigma) {
        super(centre.minus(sigma), centre.plus(sigma));
        this.centre = centre;
        this.sigma = sigma;
    }

    public Range startingFrom(DateTime time) {
        return new Range(time, time.plus(sigma).plus(sigma));
    }

    public Range startingTomorrow() {
        return startingFrom(from.plus(Duration.standardDays(1)));
    }

    public Range startingYesterday() {
        return startingFrom(from.minus(Duration.standardDays(1)));
    }

    @Override
    public boolean overlaps(TimeRegion region, Comparison comparison) {
        if (contains(region, comparison)) {
            return true;
        }

        if (region.contains(from, comparison) || region.contains(to, comparison)) {
            return true;
        }

        return region.contains(from, Comparison.INCLUSIVE) &&
                region.contains(to, Comparison.INCLUSIVE);
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
    public TimeRegion startingFrom(int year, int monthOfYear, int dayOfMonth) {
        return startingFrom(from.withDate(year, monthOfYear, dayOfMonth));
    }

    @Override
    protected boolean isIn(TimeRegion region, Comparison comparison) {
        return region.contains(from, comparison) && region.contains(to, comparison);
    }
}
