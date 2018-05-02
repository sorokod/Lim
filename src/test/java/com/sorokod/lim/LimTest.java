package com.sorokod.lim;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;

import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LimTest {

    private final Lim subject;


    @Parameters(name = "{index}: test(timeWindowSize={0}")
    public static Collection<Object[]> data() {
        return asList(new Object[][]{{1}, {2}});
    }


    public LimTest(int timeWindowSize) {
        this.subject = Lim.builder().quota(100).timeWindow(timeWindowSize, SECONDS).build();
    }


    @Test
    public void requestTokens_0() {
        assertTrue(subject.get(0));
    }


    @Test
    public void requestTokens_1() {
        assertTrue(subject.get(1));
    }


    @Test
    public void requestTokens_quota() {
        assertTrue(subject.get(subject.getSettings().quota()));
    }

    @Test
    public void requestTokens_quota_overQuota() {

        assertTrue(subject.get(subject.getSettings().quota()));
        assertFalse(subject.get(1));
    }


    @Test
    public void requestTokens_overQuota() {
        assertFalse(subject.get(subject.getSettings().quota() + 1));
    }


    @Test
    public void requestTokens_OverQuota_Sleep_AndRequestAgain() throws InterruptedException {

        subject.get(subject.getSettings().quota());
        assertFalse(subject.get(1));

        sleep(subject.getSettings().timeWindow().getSizeInMillis());

        assertTrue(subject.get(subject.getSettings().quota()));
    }
}