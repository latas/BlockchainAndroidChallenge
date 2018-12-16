package com.blockchain.btctransactions.core.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider : BaseSchedulerProvider {

    override fun io(): Scheduler =
        Schedulers.io()

    override fun ui(): Scheduler =
        AndroidSchedulers.mainThread()

}