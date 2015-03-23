package com.robwilliamson.healthyesther.util.time;

import org.joda.time.ReadableInstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RangeSet extends TimeRegion {
    private final Set<TimeRegion> mTimeRegions;

    public RangeSet(TimeRegion... timeRegions) {
        mTimeRegions = new HashSet<TimeRegion>(Arrays.asList(timeRegions));
    }

    @Override
    public boolean overlaps(TimeRegion region, Comparison comparison) {
        for (TimeRegion subRegion : mTimeRegions) {
            if (subRegion.overlaps(region, comparison)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(TimeRegion region, Comparison comparison) {
        for (TimeRegion subRegion : mTimeRegions) {
            if (!subRegion.overlaps(region, comparison)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean contains(ReadableInstant instant, Comparison comparison) {
        for (TimeRegion subRegion : mTimeRegions) {
            if (subRegion.contains(instant, comparison)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isIn(TimeRegion region, Comparison comparison) {
        for (TimeRegion subRegion : mTimeRegions) {
            if (!region.contains(subRegion, comparison)) {
                return false;
            }
        }

        return true;
    }
}
