package com.blockchain.btctransactions.data

data class TransactionItem(
    val hash: String,
    val date: String,
    val address: String,
    val amount: Double,
    val type: TransactionType
)

enum class TransactionType {
    INCOMING, OUTGOING
}
