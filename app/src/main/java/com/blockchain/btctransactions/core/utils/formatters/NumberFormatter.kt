package com.blockchain.btctransactions.core.utils.formatters

import java.math.BigDecimal

class NumberFormatter {
    fun Number.roundToFractionDigits(numFractionDigits: Int): BigDecimal =
        String.format("%.${numFractionDigits}f", toDouble()).toBigDecimal().stripTrailingZeros()

}
