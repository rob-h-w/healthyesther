package com.robwilliamson.healthyesther.util.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RangeSet extends TimeRegion {
    private final Set<TimeRegion> mTimeRegions;

    public RangeSet(TimeRegion... timeRegions) {
        super(getFrom(timeRegions), getTo(timeRegions));
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
    public RangeSet startingFrom(int year, int monthOfYear, int dayOfMonth) {
        Duration shift = Duration.millis(from.withDate(year, monthOfYear, dayOfMonth).getMillis() - from.getMillis());
        Set<TimeRegion> regions = new HashSet<>(mTimeRegions.size());

        for(TimeRegion region : mTimeRegions) {
            DateTime newFrom = region.from.plus(shift);
            regions.add(region.startingFrom(newFrom.getYear(), newFrom.getMonthOfYear(), newFrom.getDayOfMonth()));
        }

        return new RangeSet(regions.toArray(new TimeRegion[] {}));
    }

    public ReadableInstant getEdgeAfter(ReadableInstant instant) {
        ReadableInstant edge = null;

        for (TimeRegion subRegion : mTimeRegions) {
            ReadableInstant nextEdge = null;
            if (subRegion instanceof RangeSet) {
                nextEdge = ((RangeSet) subRegion).getEdgeAfter(instant);
            } else {
                if (subRegion.from.isAfter(instant)) {
                    nextEdge = subRegion.from;
                }

                if (subRegion.to.isAfter(instant)) {
                    nextEdge = subRegion.to;
                }
            }

            if (nextEdge != null) {
                if (edge != null) {
                    if (edge.isAfter(nextEdge)) {
                        edge = nextEdge;
                    }
                } else {
                    edge = nextEdge;
                }
            }
        }

        return edge;
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

    private static DateTime getFrom(TimeRegion... timeRegions) {
        DateTime from = null;

        for (TimeRegion region : timeRegions) {
            if (from == null || region.from.isBefore(from)) {
                from = region.from;
            }
        }

        return from;
    }

    private static DateTime getTo(TimeRegion... timeRegions) {
        DateTime to = null;

        for (TimeRegion region : timeRegions) {
            if (to == null || region.to.isAfter(to)) {
                to = region.to;
            }
        }

        return to;
    }
}
