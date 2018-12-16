package com.blockchain.btctransactions.domain

import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.data.Wallet
import io.reactivex.Observable
import javax.inject.Inject

class GetWalletInfoUseCase @Inject constructor() {
    fun execute(parameter: String): Observable<Result<Wallet>> = Observable.empty()
}