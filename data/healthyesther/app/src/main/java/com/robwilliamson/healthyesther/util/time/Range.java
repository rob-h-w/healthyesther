package com.robwilliamson.healthyesther.util.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;

public class Range {
    public final DateTime from;
    public final DateTime to;
    public final DateTime centre;
    public final Duration sigma;

    public enum Comparison {
        INCLUSIVE,
        EXCLUSIVE
    }

    public Range(DateTime from, DateTime to) {
        this.from = from.isBefore(to) ? from : to;
        this.to = to.isAfter(from) ? to : from;
        this.sigma = Duration.millis((this.to.getMillis() - this.from.getMillis())/2);
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

    public boolean overlaps(Range range) {
        return overlaps(range, Comparison.INCLUSIVE);
    }

    public boolean overlaps(Range range, Comparison comparison) {
        if (contains(range, comparison)) {
            return true;
        }

        if (contains(range.from, comparison) || contains(range.to, comparison)) {
            return true;
        }

        if (comparison == Comparison.EXCLUSIVE) {
            return false;
        }

        return to.isEqual(range.from) || from.isEqual(range.to);
    }

    public boolean contains(Range range) {
        return contains(range, Comparison.INCLUSIVE);
    }

    public boolean contains(Range range, Comparison comparison) {
        return contains(range.from, comparison) && contains(range.to, comparison);
    }

    public boolean contains(ReadableInstant time) {
        return contains(time, Comparison.INCLUSIVE);
    }

    public boolean contains(ReadableInstant time, Comparison comparison) {
        if (time.isAfter(from) && time.isBefore(to)) {
            return true;
        }

        if (comparison == Comparison.EXCLUSIVE) {
            return false;
        }

        return time.isEqual(from) || time.isEqual(to);
    }
}
