package com.blockchain.btctransactions.domain

import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.data.Wallet
import io.reactivex.Observable

class GetWallet : GetWalletInfoUseCase {
    override fun execute(parameter: String): Observable<Result<Wallet>> {
        return Observable.empty()
    }
}