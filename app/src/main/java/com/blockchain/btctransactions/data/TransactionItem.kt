package com.blockchain.btctransactions.data

data class TransactionItem(
    val hash: String,
    val date: String,
    val addresses: List<String>,
    val amount: String,
    val fee: String,
    val type: TransactionType
)

@DslMarker
annotation class WalletDsl

@WalletDsl
fun wallet(setup: WalletBuilder.() -> Unit): Wallet =
    WalletBuilder().apply(setup).build()


@WalletDsl
class WalletBuilder {
    lateinit var balance: String
    private val transactions = mutableListOf<TransactionItem>()

    operator fun TransactionItem.unaryPlus() {
        transactions += this
    }

    fun transaction(setup: TransactionBuilder.() -> Unit = {}) {
        val transactionBuilder = TransactionBuilder().apply(setup)
        transactions += transactionBuilder.build()
    }

    fun build(): Wallet {
        return Wallet(balance, transactions)
    }
}

@WalletDsl
class TransactionBuilder {
    private val addresses = mutableListOf<String>()

    lateinit var amount: String
    lateinit var type: TransactionType
    lateinit var hash: String
    lateinit var date: String
    lateinit var fee: String

    fun address(address: String, setup: AddressBuilder.() -> Unit = {}){
        val builder = AddressBuilder(address).apply(setup)
        addresses += builder.build()
    }


    fun build(): TransactionItem {
        return TransactionItem(hash = hash, type = type, date = date, addresses = addresses, amount = amount, fee = fee)
    }

}

@WalletDsl
class AddressBuilder(private val address: String) {

    fun build(): String {
        return address
    }
}

enum class TransactionType {
    INCOMING, OUTGOING
}
