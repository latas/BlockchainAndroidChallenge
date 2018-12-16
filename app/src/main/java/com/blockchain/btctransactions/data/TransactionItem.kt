package com.blockchain.btctransactions.data

data class TransactionItem(
    val hash: String,
    val date: String,
    val addresses: List<String>,
    val amount: String,
    val type: TransactionType
)

@DslMarker
annotation class SimpleDsl1

@SimpleDsl1
fun wallet(setup: WalletBuilder.() -> Unit): Wallet {
    val walletBuilder = WalletBuilder()
    walletBuilder.setup()
    return walletBuilder.build()
}

@SimpleDsl1
class WalletBuilder {
    lateinit var balance: String
    private val transactions = mutableListOf<TransactionItem>()

    operator fun TransactionItem.unaryPlus() {
        transactions += this
    }

    fun transaction(setup: TransactionBuilder.() -> Unit = {}) {
        val houseBuilder = TransactionBuilder()
        houseBuilder.setup()
        transactions += houseBuilder.build()
    }

    fun build(): Wallet {
        return Wallet(balance, transactions)
    }
}

@SimpleDsl1
class TransactionBuilder {
    private val addresses = mutableListOf<String>()

    lateinit var amount: String
    lateinit var type: TransactionType
    lateinit var hash: String
    lateinit var date: String

    fun address(address: String, setup: AddressBuilder.() -> Unit = {}) {
        val addressBuilder = AddressBuilder(address)
        addressBuilder.setup()
        addresses += addressBuilder.build()
    }

    fun build(): TransactionItem {
        return TransactionItem(hash, date, addresses, amount, type)
    }

}

@SimpleDsl1
class AddressBuilder(private val address: String) {

    fun build(): String {
        return address
    }
}

enum class TransactionType {
    INCOMING, OUTGOING
}
