package com.blockchain.btctransactions.utils.formatters

import com.blockchain.btctransactions.core.utils.bitcoin
import com.blockchain.btctransactions.core.utils.satoshis
import org.junit.Assert
import org.junit.Test

class CryptoAmountTest {

    @Test
    fun oneSatoshisToBitcoin() {
        val satoshis = 1.toDouble().satoshis
        Assert.assertEquals(3.35462627902512E-4, satoshis.toBitcoin.value, 0.toDouble())
    }

    @Test
    fun oneBitcoinToSatoshis() {
        val bitcoin = 1.toDouble().bitcoin
        Assert.assertEquals(2980.957987041727, bitcoin.toSatoshis.value, 0.toDouble())
    }
}