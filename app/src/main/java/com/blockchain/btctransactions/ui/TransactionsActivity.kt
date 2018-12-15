package com.blockchain.btctransactions.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blockchain.btctransactions.R
import dagger.android.AndroidInjection

class TransactionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
    }
}
