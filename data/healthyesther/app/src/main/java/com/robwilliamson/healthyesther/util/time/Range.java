package com.robwilliamson.healthyesther.util.time;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.ReadableInstant;

import javax.annotation.Nonnull;

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

    @Nonnull
    public static Builder Starting(@Nonnull DateTime dateTime) {
        return new Builder(dateTime);
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

    public Duration length() {
        return new Duration(from, to);
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

    public static class Builder {
        @Nonnull
        private final DateTime mStarting;

        public Builder(@Nonnull DateTime starting) {
            mStarting = starting.withZone(Utils.Time.localNow().getZone()).withMillisOfDay(0);
        }

        @Nonnull
        public Range from(@Nonnull HealthScoreJudgmentRangeTable.Row healthScoreJudgmentRange) {
            Long start = healthScoreJudgmentRange.getStartTime();
            Long end = healthScoreJudgmentRange.getEndTime();

            if (start == null || end == null) {
                return new Range(mStarting, mStarting.plusDays(1));
            } else {
                return new Range(mStarting.plusMillis(start.intValue()), mStarting.plusMillis(end.intValue()));
            }
        }
    }
}
