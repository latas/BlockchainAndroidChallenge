package com.blockchain.btctransactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blockchain.btctransactions.core.data.Resource
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(getWalletUseCase: GetWalletInfoUseCase) : ViewModel() {

    //input
    val multiAddrParameter = BehaviorSubject.create<String>()

    private val bag = CompositeDisposable()
    private val wallet = MutableLiveData<Resource<Wallet>>()

    val balance: LiveData<String> = Transformations.map(wallet) { resource ->
        when (resource) {
            is Resource.Success -> resource.data.balance.toString()
            else -> String()
        }
    }

    init {
        multiAddrParameter.distinct().switchMap { parameter ->
            getWalletUseCase.execute(parameter)
        }.subscribe {
            wallet.postValue(it)
        }.addTo(bag)
    }

    override fun onCleared() {
        bag.clear()
        super.onCleared()
    }
}