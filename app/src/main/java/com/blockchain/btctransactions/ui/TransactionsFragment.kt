package com.blockchain.btctransactions.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.databinding.FragmentTransactionsBinding
import com.blockchain.btctransactions.viewmodel.AppViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class TransactionsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val transactionsAdapter = TransactionsAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TransactionsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: FragmentTransactionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_transactions, container, false
        )
        binding.let {
            it.viewModel = viewModel
            it.setLifecycleOwner(this)
        }

        binding.btnRetry.setOnClickListener {
            viewModel.retryTriggered.onNext(Unit)
        }
        binding.swipeContainer.setOnRefreshListener {
            viewModel.pullToRefreshTriggered.onNext(Unit)
        }
        setUpRecyclerView(binding.rvTransactions)

        return binding.root
    }


    private fun setUpRecyclerView(rvTransactions: RecyclerView) {
        rvTransactions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTransactions.adapter = transactionsAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showErrorDialog.observe(this, Observer {
            val alertDialog = AlertDialog.Builder(activity).create()
            alertDialog.setTitle("Error")
            alertDialog.setMessage(it)
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
        })

        viewModel.transactionItemsViewModel.observe(this, transactionsAdapter)
        viewModel.viewLoadTriggered.onNext(Unit)
    }
}