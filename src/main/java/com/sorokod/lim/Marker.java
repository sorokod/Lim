package com.sorokod.lim;

import static java.lang.System.currentTimeMillis;

final class Marker {

    final long timestamp = currentTimeMillis();
    final long count;


    Marker(long count) {
        this.count = count;
    }
}
