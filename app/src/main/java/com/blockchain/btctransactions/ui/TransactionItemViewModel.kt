package com.blockchain.btctransactions.ui

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.data.TransactionItem
import com.blockchain.btctransactions.data.TransactionType

data class TransactionItemViewModel(private val transactionItem: TransactionItem) {
    val hash: String
        get() = transactionItem.hash
    val addresses: String
        get() = transactionItem.addresses.joinToString("\n")

    val fee: String
        get() = transactionItem.fee

    val amount: String
        get() = transactionItem.amount

    val date: String
        get() = transactionItem.date

    val amountBackground: Int
        get() = if (transactionItem.type == TransactionType.INCOMING) R.drawable.green_rounded_background else R.drawable.red_rounded_background

}