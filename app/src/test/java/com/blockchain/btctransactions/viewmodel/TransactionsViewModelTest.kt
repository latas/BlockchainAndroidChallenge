package com.blockchain.btctransactions.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import com.blockchain.btctransactions.ui.TransactionsViewModel
import com.blockchain.btctransactions.wallet_with_no_transactions
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception

class TransactionsViewModelTest {

    @Mock
    private lateinit var getWalletInfoUseCase: GetWalletInfoUseCase
    @Mock
    private lateinit var balanceObserver: Observer<String>
    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

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
    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Result.Success(
                    wallet_with_no_transactions
                )
            )
        )

        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(balanceObserver).onChanged(wallet_with_no_transactions.balance.toString())
        Mockito.verifyNoMoreInteractions(balanceObserver)

    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturnedAfterLoading() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(
                    wallet_with_no_transactions
                )
            )
        )
        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(balanceObserver, Mockito.times(2))
            .onChanged(Mockito.anyString())

        Assert.assertEquals(wallet_with_no_transactions.balance.toString(), viewModel.balance.value)
    }

    @Test
    fun balanceIsEmptyWhenErrorIsReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Result.Error(Exception())
            )
        )
        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(balanceObserver)
            .onChanged(String())
        Assert.assertEquals(String(), viewModel.balance.value)
        Mockito.verifyNoMoreInteractions(balanceObserver)

    }

    @Test
    fun loadingIsTrueWhenLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Result.Loading
            )
        )
        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(loadingObserver)
            .onChanged(true)
        Mockito.verifyNoMoreInteractions(loadingObserver)
    }

    @Test
    fun loadingIsTrueAndThenFalseWhenSuccessAfterLoadingReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Result.Loading, Result.Success(wallet_with_no_transactions)
            )
        )
        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(loadingObserver)
            .onChanged(true)
        Mockito.verify(loadingObserver)
            .onChanged(false)

        Assert.assertFalse(viewModel.loading.value!!)
    }
}