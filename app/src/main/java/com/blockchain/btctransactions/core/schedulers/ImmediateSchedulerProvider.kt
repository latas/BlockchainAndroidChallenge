package com.blockchain.btctransactions.core.schedulers

import io.reactivex.schedulers.Schedulers
import io.reactivex.Scheduler


class ImmediateSchedulerProvider : BaseSchedulerProvider {


    override fun computation(): Scheduler =
        Schedulers.trampoline()


    override fun io(): Scheduler =
        Schedulers.trampoline()


    override fun ui(): Scheduler =
         Schedulers.trampoline()

}