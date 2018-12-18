package com.blockchain.btctransactions.repository

import com.blockchain.btctransactions.core.api.AppService
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.schedulers.BaseSchedulerProvider
import com.blockchain.btctransactions.core.utils.toResult
import com.blockchain.btctransactions.core.utils.withLoading
import com.blockchain.btctransactions.data.MultiAddressData
import com.blockchain.btctransactions.di.scopes.PerApplication
import io.reactivex.Observable
import javax.inject.Inject

@PerApplication
class AddressRepository @Inject constructor(
    private val appService: AppService,
    private val schedulerProvider: BaseSchedulerProvider
) {

    fun getAddresses(xPub: String): Observable<Result<MultiAddressData>> =
        appService.multiAddress(xPub).toResult().withLoading()
            .subscribeOn(schedulerProvider.io())
            .observeOn(
                schedulerProvider.ui()
            )

}