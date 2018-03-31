/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.util.time;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RangeSet extends TimeRegion {
    private final Set<TimeRegion> mTimeRegions;

    public RangeSet(TimeRegion... timeRegions) {
        super(getFrom(timeRegions), getTo(timeRegions));
        mTimeRegions = new HashSet<>(Arrays.asList(timeRegions));
    }

    private static ZonedDateTime getFrom(TimeRegion... timeRegions) {
        ZonedDateTime from = null;

        for (TimeRegion region : timeRegions) {
            if (from == null || region.from.isBefore(from)) {
                from = region.from;
            }
        }

        return from;
    }

    private static ZonedDateTime getTo(TimeRegion... timeRegions) {
        ZonedDateTime to = null;

        for (TimeRegion region : timeRegions) {
            if (to == null || region.to.isAfter(to)) {
                to = region.to;
            }
        }

        return to;
    }

    private static boolean afterOrEqualTo(Instant lhs, Instant rhs) {
        return lhs.isAfter(rhs) || lhs.equals(rhs);
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
    public boolean contains(Instant instant, Comparison comparison) {
        for (TimeRegion subRegion : mTimeRegions) {
            if (subRegion.contains(instant, comparison)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RangeSet startingFrom(int year, int monthOfYear, int dayOfMonth) {
        Duration shift = Duration.of(
                from.withYear(year)
                        .withMonth(monthOfYear)
                        .withDayOfMonth(dayOfMonth).toInstant().toEpochMilli()
                        - from.toInstant().toEpochMilli(), ChronoUnit.MILLIS);
        Set<TimeRegion> regions = new HashSet<>(mTimeRegions.size());

        for (TimeRegion region : mTimeRegions) {
            ZonedDateTime newFrom = region.from.plus(shift);
            regions.add(region.startingFrom(newFrom.getYear(), newFrom.getMonthValue(), newFrom.getDayOfMonth()));
        }

        return new RangeSet(regions.toArray(new TimeRegion[]{}));
    }

    public ZonedDateTime getEdgeAfter(Instant instant) {
        ZonedDateTime edge = null;

        for (TimeRegion subRegion : mTimeRegions) {
            ZonedDateTime nextEdge = null;
            if (subRegion instanceof RangeSet) {
                nextEdge = ((RangeSet) subRegion).getEdgeAfter(instant);
            } else {
                if (afterOrEqualTo(subRegion.from.toInstant(), instant)) {
                    nextEdge = subRegion.from;
                } else if (afterOrEqualTo(subRegion.to.toInstant(), instant)) {
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
}
