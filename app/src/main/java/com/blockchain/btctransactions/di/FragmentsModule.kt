package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.ui.TransactionsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {
    @ContributesAndroidInjector
    abstract fun contributesTransactionsFragment(): TransactionsFragment
}