package com.blockchain.btctransactions.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
    private lateinit var loadingObserver: Observer<Boolean>
    @Mock
    private lateinit var refreshStopObserver: Observer<Unit?>
    @Mock
    private lateinit var pullToRefreshEnabledObserver: Observer<Boolean>
    @Mock
    private lateinit var transactionItemsObserver: Observer<List<TransactionItemViewModel>>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TransactionsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TransactionsViewModel(getWalletInfoUseCase)
        viewModel.balance.observeForever(balanceObserver)
        viewModel.loading.observeForever(loadingObserver)
        viewModel.pullToRefreshEnabled.observeForever(pullToRefreshEnabledObserver)
        viewModel.refreshStopped.observeForever(refreshStopObserver)
        viewModel.transactionItemsViewModel.observeForever(transactionItemsObserver)
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
            .onChanged(wallet_with_no_transactions.balance.toString())

        Assert.assertEquals(wallet_with_no_transactions.balance.toString(), viewModel.balance.value)
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

        Mockito.verify(loadingObserver)
            .onChanged(true)
        Mockito.verifyNoMoreInteractions(loadingObserver)
    }

    @Test
    fun loadingIsTrueAndThenFalseWhenSuccessAfterLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)
        Mockito.verify(loadingObserver)
            .onChanged(true)
        Mockito.verify(loadingObserver)
            .onChanged(false)

        Assert.assertFalse(viewModel.loading.value!!)
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
    fun loadingIsChangedOnceWhenAddrParameterGivenAndPulltoRefreshTriggered() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading
            )
        )

        viewModel.multiAddrParameter.onNext(testXPub)
        viewModel.pullToRefreshTriggered.onNext(Unit)

        Mockito.verify(getWalletInfoUseCase, Mockito.times(2)).execute(testXPub)
        Mockito.verify(loadingObserver).onChanged(true)
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
    fun testTransactionItemsListIsNotTriggeredWhenSuccessIsNotReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Loading, Result.Error(java.lang.Exception())
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(transactionItemsObserver, Mockito.never()).onChanged(Mockito.any())
    }

    @Test
    fun testTransactionItemsListIsTriggeredWhenSuccessIsReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(testXPub)).thenReturn(
            Observable.just(
                Result.Success(wallet_with_transactions)
            )
        )
        viewModel.multiAddrParameter.onNext(testXPub)

        Mockito.verify(transactionItemsObserver)
            .onChanged(wallet_with_transactions.transactionItems.map { TransactionItemViewModel(it) })
    }
}