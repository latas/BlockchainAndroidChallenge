package com.blockchain.btctransactions

import com.blockchain.btctransactions.data.*
import com.google.gson.Gson
import okio.Okio

val testXPub =
    "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"

val wallet_with_no_transactions = Wallet(
    "31.54988324", listOf()
)

val wallet_with_transactions = wallet {
    balance = "12.12435"
    transaction {
        amount = "213.324"
        hash = "cxzcwepowlfj"
        type = TransactionType.INCOMING
        date = "23/2/2018"
        fee = "12.214432"
        address("adgaksldgsdafasfdoagdjpaudsgwaiu")
        address("adgaksldsrwsfgoagdjpauds[ g[wai")
        address("adgaksldgoagdjpauds[ g[wai")
        address("adgaksldgoagdjpauds[ g[wai")

    }
    transaction {
        amount = "213.324"
        hash = "cxzcwepowlfj"
        type = TransactionType.INCOMING
        date = "23/2/2018"
        fee = "12.214432"
        address("adgaksldgoagdjpauds[ g[wai")
        address("adgaksldgoagdjpauds[ g[wai")
        address("adgaksldgoagdjpauds[ g[wai")
    }

}

object TestData {
    val multiAddressData: MultiAddressData
        get() {
            val inputStream = javaClass.classLoader
                .getResourceAsStream("api-response/multiaddress_sample_response.json")
            val source = Okio.buffer(Okio.source(inputStream))
            return Gson().fromJson(source.readString(Charsets.UTF_8), MultiAddressData::class.java)
        }
}

