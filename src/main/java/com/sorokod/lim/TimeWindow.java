package com.sorokod.lim;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Holds the size of the time window - size and time unit. Internally, size is represented
 * in milliseconds. Valid time units are: HOURS, MINUTES and SECONDS.
 */
final class TimeWindow {

    private final static EnumSet<TimeUnit> VALID_TIME_UNITS = EnumSet.of(HOURS, MINUTES, SECONDS);

    private final int size;
    private final TimeUnit timeUnit;
    private final long sizeInMillis;


    TimeWindow(int size, TimeUnit timeUnit) {

        if (!VALID_TIME_UNITS.contains(timeUnit) ) {
            throw new IllegalArgumentException("Supported TimeUnits are: " + VALID_TIME_UNITS);
        }

        this.size = size;
        this.timeUnit = timeUnit;
        this.sizeInMillis = timeUnit.toMillis(size);
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
