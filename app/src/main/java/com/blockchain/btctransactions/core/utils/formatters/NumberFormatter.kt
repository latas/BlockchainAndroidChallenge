package com.blockchain.btctransactions.core.utils.formatters

class NumberFormatter {
    fun Number.roundToFractionDigits(numFractionDigits: Int): String =
        String.format("%.${numFractionDigits}f", toDouble())
}