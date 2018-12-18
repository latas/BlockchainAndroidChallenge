package com.blockchain.btctransactions.utils.formatters

import bitcoin
import org.junit.Assert
import org.junit.Test
import satoshis

class CryptoUnitsTest {

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