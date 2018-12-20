package com.blockchain.btctransactions.ui

import androidx.recyclerview.widget.DiffUtil

class TransactionsDiffCallback(
    private val newTransactions: List<TransactionItemViewModel>,
    private val oldTransactions: List<TransactionItemViewModel>
) : DiffUtil.Callback() {
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newTransactions[newItemPosition] == oldTransactions[oldItemPosition]

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newTransactions[newItemPosition].hash == oldTransactions[oldItemPosition].hash


    override fun getOldListSize(): Int =
        oldTransactions.size

    override fun getNewListSize(): Int =
        newTransactions.size

}