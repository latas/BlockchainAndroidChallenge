package com.blockchain.btctransactions.data

data class TransactionItem(
    val hash: String,
    val date: String,
    val address: String,
    val amount: String,
    val type: TransactionType
)

class TransactionItemBuilder {
    lateinit var hash: String
    lateinit var date: String
    lateinit var amount: String
    lateinit var address: String
    lateinit var type: TransactionType


    fun build(): TransactionItem =
        TransactionItem(hash, date, address, amount, type)

}

fun transaction(block: TransactionItemBuilder.() -> Unit): TransactionItem =
    TransactionItemBuilder().apply(block).build()


enum class TransactionType {
    INCOMING, OUTGOING
}
