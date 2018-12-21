package com.blockchain.btctransactions

import com.blockchain.btctransactions.data.MultiAddressData
import com.blockchain.btctransactions.data.TransactionType
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.data.wallet
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
        date = "2018-11-12 15:32:00"
        fee = "12.214432"
        address("adgaksldgsdafasfdoagdjpaudsgwaiu")
        address("adgaksldsrwsfgoagdjpauds")
        address("adgaksldgoagdjpauds232")
        address("adgakswerldgoagdjpauds232")

    }
    transaction {
        amount = "213.324"
        hash = "cxzcwepowlfj"
        type = TransactionType.OUTGOING
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
    val jsonWallet = wallet {
        balance = "0.00621236 BTC"
        transaction {
            hash = "4524ce25c3134b42970dd94c6d2096a81dc9fb7381b986fe5eb57d98ede7655d"
            date = "2018-11-19 15:04:00"
            fee = "0.00017996 BTC"
            amount = "0.00559182 BTC"
            type = TransactionType.INCOMING
        }
        transaction {
            hash = "85ec49c884f9ebce4a713032dbf73834ab756f79c2e02294450fe92f8a817d25"
            date = "2018-11-14 14:25:48"
            fee = "0.00009816 BTC"
            amount = "-0.00017791 BTC"
            type = TransactionType.OUTGOING
            address("1CfYnV12Do6F1faqE4yzHPWLKyxExrYjih")
        }

    }


}

