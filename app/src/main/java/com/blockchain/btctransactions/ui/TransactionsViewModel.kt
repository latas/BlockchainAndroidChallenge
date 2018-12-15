package com.blockchain.btctransactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(getWalletUseCase: GetWalletInfoUseCase) : ViewModel() {
    private val bag = CompositeDisposable()
    private val wallet = MutableLiveData<Result<Wallet>>()

    //input
    val multiAddrParameter: BehaviorSubject<String> = BehaviorSubject.create<String>()
    val pullToRefreshTriggered: PublishSubject<Unit> = PublishSubject.create<Unit>()

    //outputs
    val balance: LiveData<String> = Transformations.map(wallet) { resource ->
        when (resource) {
            is Result.Success -> resource.data.balance.toString()
            else -> String()
        }
    }

    val loading: LiveData<Boolean> = Transformations.map(wallet) { resource ->
        resource.isLoading
    }

    val pullToRefreshEnabled: LiveData<Boolean> = Transformations.map(loading) {
        it.not()
    }

    val refreshStopped: LiveData<Unit?> = Transformations.map(wallet) { result ->
        if (result.isLoading.not()) {
            Unit
        }
    }

    private val pullToRefreshObservable =
        pullToRefreshTriggered.withLatestFrom(multiAddrParameter).map { (_, parameter) ->
            parameter to false
        }

    private val multiAddressInputObservable = multiAddrParameter.distinct().map { parameter ->
        parameter to true
    }

    init {
        multiAddressInputObservable.mergeWith(pullToRefreshObservable).switchMap { (parameter, showLoading) ->
            getWalletUseCase.execute(parameter).filter {
                if (!showLoading) {
                    !it.isLoading
                } else {
                    true
                }
            }
        }.subscribe {
            wallet.postValue(it)
        }.addTo(bag)
    }

    override fun onCleared() {
        bag.clear()
        super.onCleared()
    }
}