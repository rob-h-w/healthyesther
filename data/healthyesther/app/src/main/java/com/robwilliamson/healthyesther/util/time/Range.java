/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.util.time;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import javax.annotation.Nonnull;

public class Range extends TimeRegion {
    public final ZonedDateTime centre;
    public final Duration sigma;

    public Range(ZonedDateTime from, ZonedDateTime to) {
        super(from, to);
        this.sigma = Duration.ofMillis(
                (this.to.toInstant().toEpochMilli() - this.from.toInstant().toEpochMilli()) / 2);
        this.centre = this.from.plus(this.sigma);
    }

    public Range(ZonedDateTime centre, Duration sigma) {
        super(centre.minus(sigma), centre.plus(sigma));
        this.centre = centre;
        this.sigma = sigma;
    }

    @Nonnull
    public static Builder Starting(@Nonnull ZonedDateTime dateTime) {
        return new Builder(dateTime);
    }

    public Range startingFrom(ZonedDateTime time) {
        return new Range(time, time.plus(sigma).plus(sigma));
    }

    public Range startingTomorrow() {
        return startingFrom(from.plus(Duration.ofDays(1)));
    }

    public Range startingYesterday() {
        return startingFrom(from.minus(Duration.ofDays(1)));
    }

    public Duration length() {
        return Duration.between(from, to);
    }

    @Override
    public boolean overlaps(TimeRegion region, Comparison comparison) {
        if (contains(region, comparison)) {
            return true;
        }

        if (region.contains(from.toInstant(), comparison)
                || region.contains(to.toInstant(), comparison)) {
            return true;
        }

        return region.contains(from.toInstant(), Comparison.INCLUSIVE) &&
                region.contains(to.toInstant(), Comparison.INCLUSIVE);
    }

    @Override
    public boolean contains(TimeRegion region, Comparison comparison) {
        return region.isIn(this, comparison);
    }

    @Override
    public boolean contains(Instant instant, Comparison comparison) {
        if (instant.isAfter(from.toInstant()) && instant.isBefore(to.toInstant())) {
            return true;
        }

        if (comparison == Comparison.EXCLUSIVE) {
            return false;
        }

        return instant.equals(from.toInstant()) || instant.equals(to.toInstant());
    }

    @Override
    public TimeRegion startingFrom(int year, int monthOfYear, int dayOfMonth) {
        return startingFrom(from
                .withYear(year)
                .withMonth(monthOfYear)
                .withDayOfMonth(dayOfMonth));
    }

    @Override
    protected boolean isIn(TimeRegion region, Comparison comparison) {
        return region.contains(from.toInstant(), comparison)
                && region.contains(to.toInstant(), comparison);
    }

    public static class Builder {
        @Nonnull
        private final ZonedDateTime mStarting;

        public Builder(@Nonnull ZonedDateTime starting) {
            mStarting = starting.withZoneSameLocal(Utils.Time.localNow().getZone())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        }

        @Nonnull
        public Range from(@Nonnull HealthScoreJudgmentRangeTable.Row healthScoreJudgmentRange) {
            Long start = healthScoreJudgmentRange.getStartTime();
            Long end = healthScoreJudgmentRange.getEndTime();

            if (start == null || end == null) {
                return new Range(mStarting, mStarting.plusDays(1));
            } else {
                return new Range(
                        mStarting.plus(start.intValue(), ChronoUnit.MILLIS),
                        mStarting.plus(end.intValue(), ChronoUnit.MILLIS));
            }
        }
    }
}
