package com.blockchain.btctransactions.domain

import com.blockchain.btctransactions.core.data.Resource
import com.blockchain.btctransactions.data.Wallet
import io.reactivex.Observable

interface GetWalletInfoUseCase {
    fun execute(parameter: String): Observable<Resource<Wallet>>
}