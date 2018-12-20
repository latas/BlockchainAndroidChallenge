package com.blockchain.btctransactions.utils.formatters

import com.blockchain.btctransactions.core.utils.bitcoin
import com.blockchain.btctransactions.core.utils.satoshis
import org.junit.Assert
import org.junit.Test

class CryptoAmountTest {

    @Test
    fun oneSatoshisToBitcoin() {
        val satoshis = 1.satoshis
        Assert.assertEquals(0.00000001, satoshis.toBitcoin.value, 0.toDouble())
    }

    @Test
    fun oneBitcoinToSatoshis() {
        val bitcoin = 1.bitcoin
        Assert.assertEquals(100000000.0, bitcoin.toSatoshis.value, 0.toDouble())
    }
}