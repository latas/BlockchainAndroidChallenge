package com.blockchain.btctransactions.utils.formatters

import com.blockchain.btctransactions.core.utils.formatters.NumberFormatter
import org.junit.Assert
import org.junit.Test

class NumberFormatterTest {
    private val formatter = NumberFormatter()

    @Test
    fun numberShouldRemainTheSameIf_X_RequestedButHasLessThan_X() {
        Assert.assertEquals(15.97.toBigDecimal(), with(formatter) {
            15.97.roundToFractionDigits(3)
        })
    }

    @Test
    fun numberShouldHaveOnly_X_If_X_Requested() {
        Assert.assertEquals(15.97324894.toBigDecimal(), with(formatter) {
            15.9732489350253245907437540231485324038439674390264326523.roundToFractionDigits(8)
        })
    }

    @Test
    fun numberShouldHaveOnly_X_If_X_RequestedAndHasOnly_X() {
        Assert.assertEquals(15.97324893.toBigDecimal(), with(formatter) {
            15.97324893.roundToFractionDigits(8)
        })
    }

    @Test
    fun numberTestInt() {
        Assert.assertEquals(15.toBigDecimal(), with(formatter) {
            15.roundToFractionDigits(8)
        })
    }
}