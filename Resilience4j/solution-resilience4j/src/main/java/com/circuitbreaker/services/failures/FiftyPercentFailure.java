package com.circuitbreaker.services.failures;

import java.util.Random;

public class FiftyPercentFailure implements IFailure {
    Random random = new Random();
    int times;
    int failedCount;

    public FiftyPercentFailure(int times) {
        this.times = times;
    }

    @Override
    public void fail() {
        if (failedCount++ < times && random.nextInt() % 2 == 0) {
            throw new RuntimeException("Operation failed");
        }
    }
}