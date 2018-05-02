package com.sorokod.lim;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/*
 * Copyright 2018 David Soroko.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class Lim {

    private final LimSettings settings;
    private final AtomicReference<Marker> marker = new AtomicReference<>(new Marker(0));


    private Lim(final LimSettings settings) {
        this.settings = settings;
    }


    public LimSettings getSettings() {
        return settings;
    }


    public boolean get(int tokenCount) {

        boolean done = false;

        while (!done) {
            final Marker currentMarker = marker.get();
            final Marker newMarker = genMarker(tokenCount, currentMarker);

            if (newMarker == currentMarker) {
                return false;
            } else {
                done = marker.compareAndSet(currentMarker, newMarker);
            }
        }
        return true;
    }


    private Marker genMarker(long tokenCount, Marker currentMarker) {

        Marker marker = currentMarker;

        if (settings.isWindowExceeded(marker.timestamp)) {
            if (settings.isWithinQuota(tokenCount)) {
                marker = new Marker(tokenCount);
            }
        } else {
            if (settings.isWithinQuota(tokenCount + currentMarker.count)) {
                marker = new Marker(tokenCount + currentMarker.count);
            }
        }
        return marker;
    }


    @Override
    public String toString() {
        return "Lim[settings=" + settings + ']';
    }


    public static Builder builder() {
        return new Builder();
    }


    // #########################
    public static class Builder {

        private final LimSettings settings = new LimSettings();


        public Builder quota(int quota) {

            settings.quota(quota);
            return this;
        }


        public Builder timeWindow(int timeWindowSize, TimeUnit timeUnit) {

            settings.timeWindow(new TimeWindow(timeWindowSize, timeUnit));
            return this;
        }


        public Lim build() {
            return new Lim(settings);
        }
    }

}