package com.blockchain.btctransactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.utils.*
import com.blockchain.btctransactions.data.TransactionItem
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.Error
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    getWalletUseCase: GetWalletInfoUseCase,
    resourceFacade: ResourceFacade
) : ViewModel() {

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
    val refreshStopped: LiveData<Unit> = Transformations.map(wallet) { result ->
        if (result.isLoading.not()) {
            Unit
        }
    }

    private val transactionItems: LiveData<List<TransactionItem>> = wallet.mapSuccess {
        it.transactionItems
    }

    val transactionItemsViewModel: LiveData<List<TransactionItemViewModel>> = Transformations.map(transactionItems) {
        it.map { item -> TransactionItemViewModel(item) }
    }

    //we want to show the error ui only when an error occurs and the list is empty otherwise show an alert dialog
    val errorUiVisible = Transformations.map(wallet.merge(transactionItems)) { (result, items) ->
        when (result) {
            is Error -> items?.isEmpty() ?: true
            else -> false
        }
    }

    val errorDialog: LiveData<String> = Transformations.map(wallet.merge(transactionItems)) { (result, items) ->
        when (result) {
            is Error -> if (items?.isEmpty() == true) resourceFacade.getString(R.string.common_error_message) else null
            else -> null
        }
    }.filterNulls().single()


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

    fun retry() {

    }

    override fun onCleared() {
        bag.clear()
        super.onCleared()
    }
}