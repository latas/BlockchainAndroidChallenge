package com.blockchain.btctransactions.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TransactionsViewModel @Inject constructor() : ViewModel() {
    val balance = MutableLiveData<String>()
}