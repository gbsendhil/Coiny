package com.binarybricks.coiny.network.schedulers

import io.reactivex.schedulers.TestScheduler

/**
 * Implementation of the [BaseSchedulerProvider]
 * making all [io.reactivex.schedulers.TestScheduler] for testing purposes.
 */
class TestScheduleProvider : BaseSchedulerProvider {

    private val scheduler: TestScheduler = TestScheduler()

    override fun computation(): TestScheduler {
        return scheduler
    }

    override fun io(): TestScheduler {
        return scheduler
    }

    override fun ui(): TestScheduler {
        return scheduler
    }
}
