package com.blockchain.btctransactions

import com.blockchain.btctransactions.data.TransactionItem
import com.blockchain.btctransactions.data.TransactionType
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.data.transaction

val testXPub =
    "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"

val wallet_with_no_transactions = Wallet(
    31.54988324, listOf()
)

val wallet_with_transactions = Wallet(
    32.63278412,
    listOf(
        transaction {
            hash = "123"
            address = "1NSiqwerqwrXxMNbaDuuQeUgLAYMxJsvRN21Z3Ftv"
            date = "22-12-2018"
            amount = "78"
            type = TransactionType.INCOMING
        },
        transaction {
            hash = "1234"
            date = "23-12-2018"
            address = "1NSiXxMNbaDuuQ1NSiXxMNbaDuuQeUgLAYMxJsvRN21Z3FtveUgLAYMxJsvRN21Z3Ftv"
            amount = "72"
            type = TransactionType.OUTGOING
        },
        transaction {
            hash = "1234578"
            date = "22-12-2018"
            amount = "783"
            address = "1NSiXxMNbaDuuQ1NSiXxMNbaDuuQeUgLAYMxJsvRN21Z3FtveUgLAYMxJsvRN21Z3Ftv"
            type = TransactionType.INCOMING
        }
    )
)
