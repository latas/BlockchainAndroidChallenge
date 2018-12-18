package com.blockchain.btctransactions.domain

import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.utils.formatters.DateFormatter
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.di.qualifiers.Xpub
import com.blockchain.btctransactions.repository.AddressRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetWalletInfoUseCase @Inject constructor(
    @Xpub xpub: String,
    dateFormatter: DateFormatter,
    amountFormatter: DateFormatter,
    addressRepository: AddressRepository
) {
    fun execute(): Observable<Result<Wallet>> = Observable.empty()
}