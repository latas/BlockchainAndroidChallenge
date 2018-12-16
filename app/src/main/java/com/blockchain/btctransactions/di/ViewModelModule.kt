package com.blockchain.btctransactions.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blockchain.btctransactions.ui.TransactionsViewModel
import com.blockchain.btctransactions.viewmodel.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ResourceModule::class])
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    abstract fun bindTransactionsViewModel(userViewModel: TransactionsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}