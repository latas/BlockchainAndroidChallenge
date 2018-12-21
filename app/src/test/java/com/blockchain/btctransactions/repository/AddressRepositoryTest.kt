package com.blockchain.btctransactions.repository

import com.blockchain.btctransactions.TestData
import com.blockchain.btctransactions.core.api.AppService
import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.schedulers.ImmediateSchedulerProvider
import com.blockchain.btctransactions.testXPub
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AddressRepositoryTest {

    @Mock
    lateinit var appService: AppService
    private val schedulerProvider = ImmediateSchedulerProvider()

    lateinit var repository: AddressRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = AddressRepository(appService, schedulerProvider)
    }

    @Test
    fun successAfterLoadingReturnedWithCorrectData() {
        Mockito.`when`(appService.multiAddress(testXPub))
            .thenReturn(Single.just(TestData.multiAddressData))
        val testObservable = repository.getAddresses(testXPub).test()

        testObservable.awaitTerminalEvent()

        testObservable.assertTerminated()
        testObservable.assertComplete()
        testObservable.assertValues(Result.Loading, Result.Success(TestData.multiAddressData))
    }

    @Test
    fun errorAfterLoadingReturnedWithCorrectData() {
        val exception = IllegalStateException()
        Mockito.`when`(appService.multiAddress(testXPub))
            .thenReturn(Single.error(exception))
        val testObservable = repository.getAddresses(testXPub).test()

        testObservable.awaitTerminalEvent()

        testObservable.assertTerminated()
        testObservable.assertComplete()
        testObservable.assertValues(Result.Loading, Result.Error(exception))
    }

    @Test
    fun resultNeverReturned() {
        Mockito.`when`(appService.multiAddress(testXPub))
            .thenReturn(Single.never())
        val testObservable = repository.getAddresses(testXPub).test()

        testObservable.assertNotComplete()
        testObservable.assertNotTerminated()
        testObservable.assertValues(Result.Loading)
    }
}