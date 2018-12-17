package com.blockchain.btctransactions.api

import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class MultiAddressApiTest : ApiTest() {

    @Test
    fun getMultiAddressServiceWithSuccess() {
        enqueueResponse("multiaddress_sample_response.json")
        val mSubscriber = service.multiAddress(Mockito.anyString()).test()
        mSubscriber.assertNoErrors()
        mSubscriber.assertComplete()
        mSubscriber.assertValueCount(1)
        //we can also do it==TestData.multiAddressData
        mSubscriber.assertValueAt(0) {
            it.wallet.finalBalance == 621236.toDouble() &&
                    it.addresses.size == 1 &&
                    it.addresses[0].address ==
                    "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ" &&
                    it.transactions.size == 1 &&
                    it.transactions[0].hash == "4524ce25c3134b42970dd94c6d2096a81dc9fb7381b986fe5eb57d98ede7655d"
            it.transactions[0].result == 559182.toDouble() &&
                    it.transactions[0].outputs.size == 1 &&
                    it.transactions[0].outputs[0].address == "1FBPzxps6kGyk2exqLvz7cRMi2odtLEVQ" &&
                    it.transactions[0].outputs[0].xPub != null &&
                    it.transactions[0].outputs[0].xPub?.key == "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"
        }

    }

    @Test
    fun getMultiAddressServiceWithError() {
        enqueueResponse("multiaddress_error_invalid_key.json", responseCode = 500)
        val mSubscriber = service.multiAddress(Mockito.anyString()).test()
        mSubscriber.assertError {
            it is HttpException &&
                    it.code() == 500
        }

        mSubscriber.assertTerminated()
        mSubscriber.assertNotComplete()
        mSubscriber.assertNoValues()
    }

    @Test
    fun getMultiAddressServiceWithSocketTimeoutError() {
        enqueueResponse("multiaddress_sample_response.json", responseCode = 200, noResponse = true)
        val mSubscriber = service.multiAddress(Mockito.anyString()).test()
        mSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS)
        mSubscriber.assertError {
            it is SocketTimeoutException
        }
        mSubscriber.assertNotComplete()
        mSubscriber.assertNoValues()
    }
}


