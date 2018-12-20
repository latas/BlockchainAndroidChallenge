package com.blockchain.btctransactions.viewmodel

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.ui.TransactionItemViewModel
import com.blockchain.btctransactions.wallet_with_transactions
import org.junit.Assert
import org.junit.Test

class TransactionsItemViewModelTest {

    @Test
    fun hash() {
        val viewModel = TransactionItemViewModel(wallet_with_transactions.transactionItems[0])
        Assert.assertEquals("cxzcwepowlfj", viewModel.hash)
    }

    @Test
    fun date() {
        val viewModel = TransactionItemViewModel(wallet_with_transactions.transactionItems[0])
        Assert.assertEquals("2018-11-12 15:32:00", viewModel.date)
    }

    @Test
    fun fee() {
        val viewModel = TransactionItemViewModel(wallet_with_transactions.transactionItems[0])
        Assert.assertEquals("12.214432", viewModel.fee)
    }

    @Test
    fun amount() {
        val viewModel = TransactionItemViewModel(wallet_with_transactions.transactionItems[0])
        Assert.assertEquals("213.324", viewModel.amount)
    }

    @Test
    fun backgroundForInComing() {
        val viewModel = TransactionItemViewModel(wallet_with_transactions.transactionItems[0])
        Assert.assertEquals(viewModel.amountBackground, R.drawable.green_rounded_background)
    }

    @Test
    fun backgroundForOutGoing() {
        val viewModel = TransactionItemViewModel(wallet_with_transactions.transactionItems[1])
        Assert.assertEquals(viewModel.amountBackground, R.drawable.red_rounded_background)
    }
}