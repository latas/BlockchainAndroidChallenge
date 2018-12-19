package com.blockchain.btctransactions.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blockchain.btctransactions.databinding.LayoutTransactionItemCellBinding

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TransactionsAdapter.ViewHolder(
            LayoutTransactionItemCellBinding.inflate(
                layoutInflater, parent, false
            )
        )

    }

    override fun getItemCount(): Int =
        5


    override fun onBindViewHolder(holder: TransactionsAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    class ViewHolder(val binding: LayoutTransactionItemCellBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind() {

        }
    }

}