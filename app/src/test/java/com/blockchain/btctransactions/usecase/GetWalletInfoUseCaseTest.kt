package com.blockchain.btctransactions.usecase

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.TestData
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.schedulers.ImmediateSchedulerProvider
import com.blockchain.btctransactions.core.utils.formatters.AmountFormatter
import com.blockchain.btctransactions.core.utils.formatters.DateFormatter
import com.blockchain.btctransactions.core.utils.formatters.NumberFormatter
import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import com.blockchain.btctransactions.repository.AddressRepository
import com.blockchain.btctransactions.testXPub
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetWalletInfoUseCaseTest {


    @Mock
    private lateinit var repository: AddressRepository
    @Mock
    private lateinit var resourceFacade: ResourceFacade
    private lateinit var useCase: GetWalletInfoUseCase

    private val dateFormatter by lazy {
        DateFormatter(resourceFacade)
    }
    private val amountFormatter by lazy {
        AmountFormatter(resourceFacade, NumberFormatter())
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase =
                GetWalletInfoUseCase(testXPub, repository, dateFormatter, amountFormatter, ImmediateSchedulerProvider())
        Mockito.`when`(resourceFacade.getString(R.string.bitcoin_symbol)).thenReturn("BTC")
    }

    @Test
    fun whenLoadingReturnedLoadingIsPropagated() {
        Mockito.`when`(repository.getAddresses(testXPub)).thenReturn(Observable.just(Result.Loading))

        val observer = useCase.execute().test()

        observer.assertTerminated()
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValues(Result.Loading)
    }


    @Test
    fun whenErrorReturnedErrorIsPropagated() {
        val exception = Exception()
        Mockito.`when`(repository.getAddresses(testXPub)).thenReturn(Observable.just(Result.Error(exception)))

        val observer = useCase.execute().test()

        observer.assertTerminated()
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValues(Result.Error(exception))
    }

    @Test
    fun whenSuccessReturnedSuccessIsPropagatedWithCorrectData() {
        Mockito.`when`(repository.getAddresses(testXPub)).thenReturn(Observable.just(Result.Success(TestData.multiAddressData)))

        val observer = useCase.execute().test()

        observer.assertTerminated()
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValues(Result.Success(TestData.jsonWallet))
    }
}