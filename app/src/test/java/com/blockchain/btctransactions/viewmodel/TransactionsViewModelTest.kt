package com.blockchain.btctransactions.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.blockchain.btctransactions.core.data.Resource
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

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TransactionsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = TransactionsViewModel(getWalletInfoUseCase)
        viewModel.balance.observeForever(balanceObserver)
    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturned() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Resource.Success(
                    wallet_with_no_transactions
                )
            )
        )

        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(balanceObserver).onChanged(wallet_with_no_transactions.balance.toString())
    }

    @Test
    fun balanceIsUpdatedWhenSuccessReturnedAfterLoading() {
        Mockito.`when`(getWalletInfoUseCase.execute(Mockito.anyString())).thenReturn(
            Observable.just(
                Resource.Loading, Resource.Success(
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
                Resource.Error(Exception())
            )
        )
        viewModel.multiAddrParameter.onNext("")
        Mockito.verify(balanceObserver)
            .onChanged(Mockito.anyString())

        Assert.assertEquals(String(), viewModel.balance.value)
    }
}