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
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    getWalletUseCase: GetWalletInfoUseCase,
    resourceFacade: ResourceFacade
) : ViewModel() {

    private val bag = CompositeDisposable()
    private val wallet = MutableLiveData<Result<Wallet>>()

    //input
    val isPullToRefreshing = MutableLiveData<Boolean>()

    val multiAddrParameter: BehaviorSubject<String> = BehaviorSubject.create<String>()
    val retryTriggered: PublishSubject<Unit> = PublishSubject.create<Unit>()
    val pullToRefreshTriggered: PublishSubject<Unit> = PublishSubject.create<Unit>()

    private val pullToRefreshTriggeredObservable =
        pullToRefreshTriggered.withLatestFrom(multiAddrParameter).map { (_, parameter) ->
            parameter
        }

    private val retryTriggeredObservable =
        retryTriggered.withLatestFrom(multiAddrParameter).map { (_, parameter) ->
            parameter
        }

    private val multiAddressInputObservable = multiAddrParameter.distinct().map { parameter ->
        parameter
    }

    private val input =
        multiAddressInputObservable.mergeWith(pullToRefreshTriggeredObservable).mergeWith(retryTriggeredObservable)

    //outputs

    private val loading: LiveData<Boolean> = Transformations.map(wallet) { resource ->
        resource.isLoading
    }

    val balance: LiveData<String> = Transformations.map(wallet) { resource ->
        when (resource) {
            is Result.Success -> resource.data.balance
            else -> String()
        }
    }

    val showLoader = Transformations.map(isPullToRefreshing.merge(loading)) { (isPullToRefreshing, isLoading) ->
        isLoading == true && !(isPullToRefreshing ?: false)
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
        input.switchMap { parameter ->
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