package com.blockchain.btctransactions.core.schedulers

import io.reactivex.Scheduler


interface BaseSchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler
}