package com.blockchain.btctransactions.domain

import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.repository.AddressRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetWalletInfoUseCase @Inject constructor(addressRepository: AddressRepository) {
    fun execute(parameter: String): Observable<Result<Wallet>> = Observable.empty()
}