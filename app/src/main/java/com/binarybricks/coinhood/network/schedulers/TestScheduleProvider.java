package com.binarybricks.coinhood.network.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.schedulers.TestScheduler;

/**
 * Implementation of the {@link BaseSchedulerProvider} making all {@link io.reactivex.schedulers.TestScheduler} for testing purposes.
 */
public class TestScheduleProvider implements BaseSchedulerProvider {

    private TestScheduler scheduler;

    public TestScheduleProvider() {
        scheduler = new TestScheduler();
    }

    @NonNull
    @Override
    public TestScheduler computation() {
        return scheduler;
    }

    @NonNull
    @Override
    public TestScheduler io() {
        return scheduler;
    }

    @NonNull
    @Override
    public TestScheduler ui() {
        return scheduler;
    }
}
