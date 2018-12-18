package com.blockchain.btctransactions.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import com.blockchain.btctransactions.testXPub
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
    private lateinit var showLoadingObserver: Observer<Boolean>
    @Mock
    private lateinit var resourceFacade: ResourceFacade
    @Mock
    private lateinit var refreshStopObserver: Observer<Unit?>
    @Mock
    private lateinit var pullToRefreshEnabledObserver: Observer<Boolean>
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
        viewModel.showLoader.observeForever(showLoadingObserver)
        viewModel.pullToRefreshEnabled.observeForever(pullToRefreshEnabledObserver)
        viewModel.refreshStopped.observeForever(refreshStopObserver)
        viewModel.transactionItemsViewModel.observeForever(transactionItemsObserver)
        viewModel.errorUiWidgetVisible.observeForever(errorUiWidgetVisibilityObserver)
        viewModel.showErrorDialog.observeForever(errorDialogObserver)
    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(
                    wallet_with_no_transactions
                )
            )
        )

        viewModel.multiAddrParameter.onNext(testXPub)
        Mockito.verify(balanceObserver).onChanged(wallet_with_no_transactions.balance.toString())
        Mockito.verifyNoMoreInteractions(balanceObserver)

    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturnedAfterLoading() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(
                    wallet_with_no_transactions
                )
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(balanceObserver)
            .onChanged(String())

        Mockito.verify(balanceObserver)
            .onChanged(wallet_with_no_transactions.balance)

        Assert.assertEquals(wallet_with_no_transactions.balance, viewModel.balance.value)
    }

    @Test
    fun balanceIsEmptyWhenErrorIsReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Error(Exception())
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(balanceObserver)
            .onChanged(String())
        Mockito.verifyNoMoreInteractions(balanceObserver)

    }

    @Test
    fun loadingIsTrueWhenLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(showLoadingObserver)
            .onChanged(true)
        Mockito.verifyNoMoreInteractions(showLoadingObserver)
    }

    @Test
    fun loadingIsTrueAndThenFalseWhenSuccessAfterLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)
        Mockito.verify(showLoadingObserver)
            .onChanged(true)
        Mockito.verify(showLoadingObserver)
            .onChanged(false)

        Assert.assertFalse(viewModel.showLoader.value!!)
    }

    @Test
    fun useCaseExecutedOnceForMultipleSameInputs() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )

        viewModel.multiAddrParameter.onNext(testXPub)
        viewModel.multiAddrParameter.onNext(testXPub)
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(getWalletInfoUseCase).execute(testXPub)
        Mockito.verifyNoMoreInteractions(getWalletInfoUseCase)
    }

    @Test
    fun nothingHappensWhenPulltoRefreshTriggeredWithoutInput() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )

        viewModel.pullToRefreshTriggered.onNext(Unit)

        Mockito.verify(getWalletInfoUseCase, Mockito.never()).execute(Mockito.anyString())
    }


    @Test
    fun showLoaderIsFalseWhenAddrParameterGivenAndPulltoRefreshTriggered() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading
            )
        )

        viewModel.multiAddrParameter.onNext(testXPub)
        viewModel.pullToRefreshTriggered.onNext(Unit)
        viewModel.isPullToRefreshing.value = true

        Mockito.verify(getWalletInfoUseCase, Mockito.times(2)).execute(testXPub)
        Assert.assertFalse(viewModel.showLoader.value!!)
    }

    @Test
    fun refreshStopTriggersWhenStateDifferentToLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(wallet_with_no_transactions)
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(refreshStopObserver).onChanged(Unit)
        Mockito.verifyNoMoreInteractions(refreshStopObserver)
    }

    @Test
    fun pullToRefreshIsNotEnabledWhenLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(pullToRefreshEnabledObserver).onChanged(false)
        Mockito.verifyNoMoreInteractions(pullToRefreshEnabledObserver)
    }

    @Test
    fun transactionItemsListIsNotTriggeredWhenSuccessIsNotReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Error(java.lang.Exception())
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(transactionItemsObserver, Mockito.never()).onChanged(Mockito.any())
    }

    @Test
    fun transactionItemsListIsTriggeredWhenSuccessIsReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions)
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(transactionItemsObserver)
            .onChanged(wallet_with_transactions.transactionItems.map { TransactionItemViewModel(it) })
    }

    @Test
    fun errorUiWidgetIsNotVisibleWhenAnErrorOccursAfterSuccess() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions), Result.Error(java.lang.Exception())
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(errorUiWidgetVisibilityObserver, Mockito.never()).onChanged(true)
        Assert.assertFalse(viewModel.errorUiWidgetVisible.value!!)
    }

    @Test
    fun errorUiWidgetIsVisibleWhenAnErrorOccursWithoutPreviousSuccess() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Error(java.lang.Exception())
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(errorUiWidgetVisibilityObserver).onChanged(true)
        Assert.assertTrue(viewModel.errorUiWidgetVisible.value!!)
    }

    @Test
    fun errorDialogTriggeredOnlyOnceWhenErrorOccursAfterSuccess() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions), Result.Error(java.lang.Exception())
            )
        )
        Mockito.`when`(resourceFacade.getString(R.string.common_error_message))
            .thenReturn("Oups! Something went wrong. You can pull to refresh to retry!")

        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(errorDialogObserver).onChanged("Oups! Something went wrong. You can pull to refresh to retry!")

        Mockito.verifyNoMoreInteractions(errorDialogObserver)
    }

    @Test
    fun errorDialogTriggeredForMultipleSubscribes() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions), Result.Error(java.lang.Exception())
            )
        )
        Mockito.`when`(resourceFacade.getString(R.string.common_error_message))
            .thenReturn("Oups! Something went wrong. You can pull to refresh to retry!")

        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(errorDialogObserver).onChanged("Oups! Something went wrong. You can pull to refresh to retry!")
    }

    @Test
    fun resultIsReturnedAfterRetrying() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Error(java.lang.Exception())
            )
        )

        viewModel.multiAddrParameter.onNext(testXPub)
        Mockito.verify(transactionItemsObserver, Mockito.never()).onChanged(Mockito.anyList())

        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
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