package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.core.schedulers.BaseSchedulerProvider
import com.blockchain.btctransactions.core.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class SchedulersModule {
    @Provides
    fun providesSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()
}