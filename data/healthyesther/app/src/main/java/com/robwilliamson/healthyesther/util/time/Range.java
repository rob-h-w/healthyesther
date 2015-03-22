package com.robwilliamson.healthyesther.util.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Range {
    public final DateTime from;
    public final DateTime to;
    public final DateTime centre;
    public final Duration sigma;

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
}
