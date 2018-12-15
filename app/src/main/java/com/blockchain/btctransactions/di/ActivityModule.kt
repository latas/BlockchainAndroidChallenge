package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.ui.TransactionsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun contributeTransactionsActivity(): TransactionsActivity
}