package com.blockchain.btctransactions.utils.formatters

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.utils.bitcoin
import com.blockchain.btctransactions.core.utils.formatters.AmountFormatter
import com.blockchain.btctransactions.core.utils.formatters.NumberFormatter
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AmountFormatterTest {
    @Mock
    private lateinit var resourceFacade: ResourceFacade

    private val numberFormatter = NumberFormatter()
    private lateinit var amountFormatter: AmountFormatter
    private val significantDigits = 8


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        amountFormatter = AmountFormatter(resourceFacade, numberFormatter)
        Mockito.`when`(resourceFacade.getString(R.string.bitcoin_symbol)).thenReturn("BTC")
    }

    @Test
    fun numberShouldRemainTheSameIf_X_RequestedButHasLessThan_X() {
        Assert.assertEquals("15.97 BTC", with(amountFormatter) {
            15.97.bitcoin.formattedRoundedToDigits(significantDigits)
        })
    }

    @Test
    fun numberShouldHaveOnly_X_If_X_Requested() {
        Assert.assertEquals("15.97324894 BTC", with(amountFormatter) {
            15.9732489350253245907437540231485324038439674390264326523.bitcoin.formattedRoundedToDigits(
                significantDigits
            )
        })
    }

    @Test
    fun numberShouldHaveOnly_X_If_X_RequestedAndHasOnly_X() {
        Assert.assertEquals("15.97324893 BTC", with(amountFormatter) {
            15.97324893.bitcoin.formattedRoundedToDigits(
                significantDigits
            )
        })
    }

    @Test
    fun numberTestInt() {
        Assert.assertEquals("15 BTC", with(amountFormatter) {
            15.bitcoin.formattedRoundedToDigits(significantDigits)
        })
    }
}