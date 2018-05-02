package com.sorokod.lim;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

final class TimeWindow {

    private final static EnumSet<TimeUnit> SUPPORTED_TIME_UNITS = EnumSet.of(HOURS, MINUTES, SECONDS);

    private final int size;
    private final TimeUnit timeUnit;
    private final long sizeInMillis;


    TimeWindow(int timeWindowSize, TimeUnit timeWindowUnit) {

        if (!SUPPORTED_TIME_UNITS.contains(timeWindowUnit) ) {
            throw new IllegalArgumentException("Supported TimeUnits are: " + SUPPORTED_TIME_UNITS);
        }

        this.size = timeWindowSize;
        this.timeUnit = timeWindowUnit;
        this.sizeInMillis = timeWindowUnit.toMillis(timeWindowSize);
    }


    int getSize() {
        return size;
    }


    TimeUnit getTimeUnit() {
        return timeUnit;
    }


    long getSizeInMillis() {
        return sizeInMillis;
    }


    @Override
    public String toString() {
        return "TimeWindow[size=" + size + "|timeUnit=" + timeUnit + ']';
    }
}
