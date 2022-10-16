package com.circuitbreaker.services.failures;

import java.util.Random;

public class GuaranteeFailure implements IFailure {
    Random random = new Random();

    public GuaranteeFailure() {
    }

    @Override
    public void fail() {
        throw new RuntimeException("Operation failed");
    }
}