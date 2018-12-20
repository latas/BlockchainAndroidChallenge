package com.blockchain.btctransactions.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.blockchain.btctransactions.databinding.LayoutTransactionItemCellBinding
import androidx.recyclerview.widget.DiffUtil


class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>(),
    Observer<List<TransactionItemViewModel>> {

    private var items = emptyList<TransactionItemViewModel>()

    override fun onChanged(newItems: List<TransactionItemViewModel>?) {
        newItems?.let {
            val diffResult = DiffUtil.calculateDiff(TransactionsDiffCallback(newItems, items))
            items = newItems
            diffResult.dispatchUpdatesTo(this)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TransactionsAdapter.ViewHolder(
            LayoutTransactionItemCellBinding.inflate(
                layoutInflater, parent, false
            )
        )

    }

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(holder: TransactionsAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val binding: LayoutTransactionItemCellBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionItemViewModel: TransactionItemViewModel) {
            binding.viewModel = transactionItemViewModel
            binding.executePendingBindings()
        }
    }

}