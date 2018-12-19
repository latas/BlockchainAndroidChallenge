package com.blockchain.btctransactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.ui.UiErrorState
import com.blockchain.btctransactions.core.utils.*
import com.blockchain.btctransactions.data.TransactionItem
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    getWalletUseCase: GetWalletInfoUseCase,
    resourceFacade: ResourceFacade
) : ViewModel() {

    private val bag = CompositeDisposable()
    private val wallet = MutableLiveData<Result<Wallet>>()
    private val requiresLoader = MutableLiveData<Boolean>()

    //inputs
    val viewLoadTriggered: BehaviorSubject<Unit> = BehaviorSubject.create<Unit>()
    val retryTriggered: PublishSubject<Unit> = PublishSubject.create<Unit>()
    val pullToRefreshTriggered: PublishSubject<Unit> = PublishSubject.create<Unit>()


    private val dataRequestInput =
        viewLoadTriggered.distinct().doOnNext {
            requiresLoader.postValue(true)
        }.mergeWith(retryTriggered.doOnNext {
            requiresLoader.postValue(true)
        }).mergeWith(pullToRefreshTriggered.doOnNext {
            requiresLoader.postValue(false)
        })

    //outputs
    private val loading: LiveData<Boolean> = Transformations.map(wallet) { resource ->
        resource.isLoading
    }

    val showLoader: LiveData<Boolean> =
        Transformations.map(loading.withLatestFrom(requiresLoader)) { (isLoading, requiresLoader) ->
            isLoading == true && requiresLoader == true
        }

    val balance: LiveData<String> = Transformations.map(wallet) { resource ->
        when (resource) {
            is Result.Success -> resource.data.balance
            else -> String()
        }
    }

    val canBeRefreshed: LiveData<Boolean> = Transformations.map(wallet) {
        it.isLoading.not()
    }

    val refreshStopped: LiveData<Boolean> = Transformations.map(wallet) { result ->
        result.isLoading.not()
    }

    private val transactionItems: LiveData<List<TransactionItem>> = wallet.mapSuccess {
        it.transactionItems
    }

    val transactionItemsViewModel: LiveData<List<TransactionItemViewModel>> = Transformations.map(transactionItems) {
        it.map { item -> TransactionItemViewModel(item) }
    }

    private val uiErrorState = Transformations.map(wallet.merge(transactionItems)) { (result, items) ->
        when (result) {
            is Result.Error -> if (items.isNullOrEmpty()) UiErrorState.JUST_ERROR else UiErrorState.ERROR_WITH_DATA
            else -> UiErrorState.NO_ERROR
        }
    }


    val errorUiWidgetVisible: LiveData<Boolean> = Transformations.map(uiErrorState) {
        it == UiErrorState.JUST_ERROR
    }

    val showErrorDialog: LiveData<String> =
        Transformations.map(uiErrorState.filter { it == UiErrorState.ERROR_WITH_DATA }) {
            resourceFacade.getString(R.string.common_error_message)
        }.single()

    init {
        dataRequestInput.switchMap {
            getWalletUseCase.execute()
        }.subscribe {
            wallet.postValue(it)
        }.addTo(bag)
    }

    override fun onCleared() {
        bag.clear()
        super.onCleared()
    }
}