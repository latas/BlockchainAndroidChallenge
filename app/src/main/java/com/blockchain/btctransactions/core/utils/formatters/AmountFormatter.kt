package com.blockchain.btctransactions.core.utils.formatters

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.utils.Bitcoin
import com.blockchain.btctransactions.core.utils.CryptoAmount
import com.blockchain.btctransactions.core.utils.bitcoin
import javax.inject.Inject

class AmountFormatter @Inject constructor(
    private val resourceFacade: ResourceFacade,
    private val numberFormatter: NumberFormatter
) {

    fun CryptoAmount<Bitcoin>.formattedRoundedToDigits(digits: Int): String =
        with(numberFormatter) {
            value.roundToFractionDigits(digits)
        }.bitcoin.value.toBigDecimal().stripTrailingZeros().toString().plus(" ").plus(resourceFacade.getString(R.string.bitcoin_symbol))
}

