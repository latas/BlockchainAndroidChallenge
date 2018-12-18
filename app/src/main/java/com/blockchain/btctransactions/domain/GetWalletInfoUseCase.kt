package com.blockchain.btctransactions.domain

import com.blockchain.btctransactions.core.data.Result
import com.blockchain.btctransactions.core.utils.formatters.DateFormatter
import com.blockchain.btctransactions.core.utils.formatters.NumberFormatter
import com.blockchain.btctransactions.core.utils.seconds
import com.blockchain.btctransactions.data.MultiAddressData
import com.blockchain.btctransactions.data.TransactionType
import com.blockchain.btctransactions.data.Wallet
import com.blockchain.btctransactions.data.wallet
import com.blockchain.btctransactions.di.qualifiers.Xpub
import com.blockchain.btctransactions.repository.AddressRepository
import io.reactivex.Observable
import satoshis
import javax.inject.Inject
import kotlin.math.absoluteValue

class GetWalletInfoUseCase @Inject constructor(
    @Xpub private val xpub: String,
    private val repository: AddressRepository,
    private val dateFormatter: DateFormatter,
    private val numberFormatter: NumberFormatter
) {
    fun execute(): Observable<Result<Wallet>> =
        repository.getAddresses(xpub).map {
            when (it) {
                is Result.Success -> Result.Success(
                    composeWallet(it.data)
                )
                is Result.Loading -> it
                is Result.Error -> it

            }
        }

    private fun composeWallet(data: MultiAddressData): Wallet {
        return wallet {
            balance = data.wallet.balance.bitcoin()
            data.transactions.map { transactionData ->
                transaction {
                    amount = transactionData.result.bitcoin()
                    type = if (transactionData.result >= 0) TransactionType.INCOMING else TransactionType.OUTGOING
                    date = with(dateFormatter) {
                        transactionData.time.seconds.toLocalDateTime()
                    }
                    hash = transactionData.hash
                    fee = transactionData.fee.bitcoin()

                    transactionData.outputs.map {
                        address(it.address)
                    }
                }
            }
        }
    }

    private fun Double.bitcoin(): String =
        with(numberFormatter) {
            this@bitcoin.satoshis.toBitcoin.value.absoluteValue.roundToFractionDigits(8)
        }
}