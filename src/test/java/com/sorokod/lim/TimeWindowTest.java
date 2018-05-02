package com.sorokod.lim;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

public class TimeWindowTest {


    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidTimeUnits() {
        new TimeWindow(1,MILLISECONDS);
    }


    @Test
    public void shouldConvertSizeToMillis() {
        assertEquals(1000, new TimeWindow(1,SECONDS).getSizeInMillis());
    }
}