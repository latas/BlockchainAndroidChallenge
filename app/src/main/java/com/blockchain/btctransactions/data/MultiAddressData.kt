package com.blockchain.btctransactions.data

import com.google.gson.annotations.SerializedName

data class MultiAddressData(
    val wallet: WalletData,
    val addresses: List<AddressData>,
    @SerializedName("txs") val transactions: List<TransactionData>
)

data class AddressData(val address: String)

data class WalletData(@SerializedName("final_balance") val balance: Double)


data class TransactionData(
    val hash: String,
    val time: Long,
    val fee: Double,
    val result: Double,
    @SerializedName("out") val outputs: List<OutputData>
)

data class OutputData(@SerializedName("addr") val address: String, @SerializedName("xpub") val xPub: XPub?)
data class XPub(@SerializedName("m") val key: String)