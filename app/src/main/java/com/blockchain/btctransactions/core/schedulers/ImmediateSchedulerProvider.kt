package com.blockchain.btctransactions.core.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


class ImmediateSchedulerProvider : BaseSchedulerProvider {


    override fun io(): Scheduler =
        Schedulers.trampoline()

    override fun ui(): Scheduler =
        Schedulers.trampoline()

}