package com.robwilliamson.healthyesther.util.time;

import org.joda.time.ReadableInstant;

public abstract class TimeRegion {
    public enum Comparison {
        INCLUSIVE,
        EXCLUSIVE
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

    protected abstract boolean isIn(TimeRegion region, Comparison comparison);
}
