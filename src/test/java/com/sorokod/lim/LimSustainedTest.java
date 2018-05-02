package com.sorokod.lim;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LimSustainedTest {

    private final int duration;
    private final int threadCount;

    private final Lim subject;
    private final ExecutorService executor;
    private final List<Worker> workers;


    @Parameters(name = "{index}: test(quota={0}, duration={1}, threadCount={2}")
    public static Collection<Object[]> data() {

        return asList(new Object[][]{
                {100, 2, 1},
                {100, 2, 2},
                {100, 2, 3},
                {100, 2, 4},
                {100, 2, 8}
        });
    }


    public LimSustainedTest(int quota, int duration, int threadCount) {
        this.duration = duration;
        this.threadCount = threadCount;

        this.executor = newFixedThreadPool(threadCount);

        this.subject = Lim.builder()
                .quota(quota)
                .timeWindow(1, SECONDS)
                .build();

        this.workers = range(0, threadCount).mapToObj(i -> new Worker()).collect(toList());
    }


    @Test
    public void test() throws InterruptedException {

        executor.invokeAll(workers);

        executor.shutdown();
        executor.awaitTermination(duration + 2, SECONDS);

        for (Callable r : workers) {
            System.out.println(r);
        }

        assertTotalSuccess(workers);
        assertIndividualSuccess(workers);
    }


    private void assertTotalSuccess(List<Worker> workers) {

        int expectedSum = subject.getSettings().quota() * duration;

        int actualSum = workers.stream()
                .mapToInt(w -> w.requestSumSuccessful)
                .sum();

        assertEquals("Unexpected sum in: " + workers, expectedSum, actualSum);
    }


    private void assertIndividualSuccess(List<Worker> workers) {

        int total = subject.getSettings().quota() * duration;
        int delta = (total / 100) * 5;
        int expectedValue = total / threadCount;

        workers.forEach(w -> assertTrue(
                "Unexpected requestSumSuccessful in: " + w,
                abs(w.requestSumSuccessful - expectedValue) <= delta)
        );
    }


    // #######################################################
    private final class Worker implements Callable<Integer> {

        long durationInMillis = SECONDS.toMillis(duration);

        int requestSum;
        int requestSumSuccessful;


        @Override
        public Integer call() throws InterruptedException {

            long elapsed = 0;
            long startTime = currentTimeMillis();

            while (elapsed < durationInMillis) {
                sleep(1);
                requestToken();
                elapsed = currentTimeMillis() - startTime;
            }

            return requestSumSuccessful;
        }


        private void requestToken() {

            requestSum++;

            if (subject.get(1)) {
                requestSumSuccessful++;
            }
        }


        @Override
        public String toString() {
            return "Worker[subject=" + subject +
                    "|duration=" + duration +
                    "|requestSum=" + requestSum +
                    "|requestSumSuccessful=" + requestSumSuccessful +
                    ']';
        }
    }
}