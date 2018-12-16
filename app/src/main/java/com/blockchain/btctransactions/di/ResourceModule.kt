package com.blockchain.btctransactions.di

import android.app.Application
import com.blockchain.btctransactions.core.data.RealResourceFacade
import com.blockchain.btctransactions.core.data.ResourceFacade
import dagger.Module
import dagger.Provides

@Module
class ResourceModule {
    @Provides
    fun providesResourceFacade(appContext: Application): ResourceFacade = RealResourceFacade(appContext)
}