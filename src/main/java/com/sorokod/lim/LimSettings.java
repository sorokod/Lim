package com.sorokod.lim;

import static java.lang.System.currentTimeMillis;

final class LimSettings {

    private int quota;
    private TimeWindow timeWindow;


    public void quota(int quota) {
        this.quota = quota;
    }


    public int quota() {
        return quota;
    }


    public void timeWindow(TimeWindow timeWindow) {
        this.timeWindow = timeWindow;
    }


    public TimeWindow timeWindow() {
        return timeWindow;
    }


    public boolean isWindowExceeded(final long timestamp) {
        return (currentTimeMillis() - timestamp) >= timeWindow.getSizeInMillis();
    }


    public boolean isWithinQuota(final long tokenCount) {
        return tokenCount <= quota;
    }


    @Override
    public String toString() {
        return "LimSettings[quota=" + quota + "|timeWindow=" + timeWindow + ']';
    }
}
