package com.blockchain.btctransactions.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import com.blockchain.btctransactions.ui.TransactionItemViewModel
import com.blockchain.btctransactions.ui.TransactionsViewModel
import com.blockchain.btctransactions.wallet_with_no_transactions
import com.blockchain.btctransactions.wallet_with_transactions
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class TransactionsViewModelTest {

    @Mock
    private lateinit var getWalletInfoUseCase: GetWalletInfoUseCase
    @Mock
    private lateinit var balanceObserver: Observer<String>
    @Mock
    private lateinit var showLoaderObserver: Observer<Boolean>
    @Mock
    private lateinit var resourceFacade: ResourceFacade
    @Mock
    private lateinit var refreshStopObserver: Observer<Boolean>
    @Mock
    private lateinit var canBeRefreshed: Observer<Boolean>
    @Mock
    private lateinit var transactionItemsObserver: Observer<List<TransactionItemViewModel>>
    @Mock
    private lateinit var errorUiWidgetVisibilityObserver: Observer<Boolean>
    @Mock
    private lateinit var errorDialogObserver: Observer<String>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TransactionsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TransactionsViewModel(getWalletInfoUseCase, resourceFacade)
        viewModel.balance.observeForever(balanceObserver)
        viewModel.showLoader.observeForever(showLoaderObserver)
        viewModel.canBeRefreshed.observeForever(canBeRefreshed)
        viewModel.refreshStopped.observeForever(refreshStopObserver)
        viewModel.transactionItemsViewModel.observeForever(transactionItemsObserver)
        viewModel.errorUiWidgetVisible.observeForever(errorUiWidgetVisibilityObserver)
        viewModel.showErrorDialog.observeForever(errorDialogObserver)
    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(
                    wallet_with_no_transactions
                )
            )
        )

        viewModel.viewLoadTriggered.onNext(Unit)
        Mockito.verify(balanceObserver).onChanged(wallet_with_no_transactions.balance)
        Mockito.verifyNoMoreInteractions(balanceObserver)

    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturnedAfterLoading() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(
                    wallet_with_no_transactions
                )
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(balanceObserver)
            .onChanged(String())

        Mockito.verify(balanceObserver)
            .onChanged(wallet_with_no_transactions.balance)

        Assert.assertEquals(wallet_with_no_transactions.balance, viewModel.balance.value)
    }

    @Test
    fun balanceIsEmptyWhenErrorIsReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Error(Exception())
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(balanceObserver)
            .onChanged(String())
        Mockito.verifyNoMoreInteractions(balanceObserver)

    }

    @Test
    fun showLoaderIsTrueWhenLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(showLoaderObserver)
            .onChanged(true)
        Mockito.verifyNoMoreInteractions(showLoaderObserver)
    }

    @Test
    fun showLoaderIsTrueAndThenFalseWhenSuccessAfterLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        val inOrder = Mockito.inOrder(showLoaderObserver)
        inOrder.verify(showLoaderObserver).onChanged(true)
        inOrder.verify(showLoaderObserver).onChanged(false)
        Mockito.verifyNoMoreInteractions(showLoaderObserver)
    }

    @Test
    fun useCaseExecutedOnceForMultipleViewLoadTriggers() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )

        viewModel.viewLoadTriggered.onNext(Unit)
        viewModel.viewLoadTriggered.onNext(Unit)
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(getWalletInfoUseCase).execute()
        Mockito.verifyNoMoreInteractions(getWalletInfoUseCase)
    }

    @Test
    fun showLoaderIsFalseWhenPulltoRefreshTriggered() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading
            )
        )

        viewModel.pullToRefreshTriggered.onNext(Unit)

        Mockito.verify(showLoaderObserver).onChanged(false)
        Mockito.verifyNoMoreInteractions(showLoaderObserver)
    }

    @Test
    fun refreshStopTriggersWhenStateDifferentToLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(wallet_with_no_transactions)
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(refreshStopObserver).onChanged(true)
        Mockito.verifyNoMoreInteractions(refreshStopObserver)
    }

    @Test
    fun pullToRefreshIsNotEnabledWhenLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(canBeRefreshed).onChanged(false)
        Mockito.verifyNoMoreInteractions(canBeRefreshed)
    }

    @Test
    fun transactionItemsListIsNotTriggeredWhenSuccessIsNotReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading, Result.Error(java.lang.Exception())
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(transactionItemsObserver, Mockito.never()).onChanged(Mockito.any())
    }

    @Test
    fun transactionItemsListIsTriggeredWhenSuccessIsReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions)
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(transactionItemsObserver)
            .onChanged(wallet_with_transactions.transactionItems.map { TransactionItemViewModel(it) })
    }

    @Test
    fun errorUiWidgetIsNotVisibleWhenAnErrorOccursAfterSuccess() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions), Result.Error(java.lang.Exception())
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(errorUiWidgetVisibilityObserver, Mockito.never()).onChanged(true)
        Assert.assertFalse(viewModel.errorUiWidgetVisible.value!!)
    }

    @Test
    fun errorUiWidgetIsVisibleWhenAnErrorOccursWithoutPreviousSuccess() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading, Result.Error(java.lang.Exception())
            )
        )
        viewModel.viewLoadTriggered.onNext(Unit)
        val inOrder = Mockito.inOrder(errorUiWidgetVisibilityObserver)
        inOrder.verify(errorUiWidgetVisibilityObserver).onChanged(false)
        inOrder.verify(errorUiWidgetVisibilityObserver).onChanged(true)
        Mockito.verifyNoMoreInteractions(errorUiWidgetVisibilityObserver)
    }

    @Test
    fun showLoaderWhenRetryTriggeredAndLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Loading
            )
        )
        viewModel.retryTriggered.onNext(Unit)
        Mockito.verify(showLoaderObserver).onChanged(true)
        Mockito.verifyNoMoreInteractions(showLoaderObserver)
    }

    @Test
    fun errorDialogTriggeredOnlyOnceWhenErrorOccursAfterSuccess() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions), Result.Error(java.lang.Exception())
            )
        )
        Mockito.`when`(resourceFacade.getString(R.string.common_error_message))
            .thenReturn("Oups! Something went wrong. You can pull to refresh to retry!")

        viewModel.viewLoadTriggered.onNext(Unit)

        Mockito.verify(errorDialogObserver).onChanged("Oups! Something went wrong. You can pull to refresh to retry!")
        Mockito.verifyNoMoreInteractions(errorDialogObserver)
    }

    @Test
    fun errorDialogTriggeredForMultipleSubscribes() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions), Result.Error(java.lang.Exception())
            )
        )
        Mockito.`when`(resourceFacade.getString(R.string.common_error_message))
            .thenReturn("Oups! Something went wrong. You can pull to refresh to retry!")

        viewModel.viewLoadTriggered.onNext(Unit)
        Mockito.verify(errorDialogObserver).onChanged("Oups! Something went wrong. You can pull to refresh to retry!")
    }

    @Test
    fun resultIsReturnedAfterRetrying() {
        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Error(java.lang.Exception())
            )
        )

        viewModel.viewLoadTriggered.onNext(Unit)
        Mockito.verify(transactionItemsObserver, Mockito.never()).onChanged(Mockito.anyList())

        Mockito.`when`(getWalletInfoUseCase.execute()).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions)
            )
        )

        viewModel.retryTriggered.onNext(Unit)

        Mockito.verify(transactionItemsObserver)
            .onChanged(wallet_with_transactions.transactionItems.map {
                TransactionItemViewModel(it)
            })
        Mockito.verify(balanceObserver).onChanged(wallet_with_transactions.balance)

    }
}