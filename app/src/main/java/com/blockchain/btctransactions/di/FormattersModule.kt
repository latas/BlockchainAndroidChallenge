package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.utils.formatters.DateFormatter
import com.blockchain.btctransactions.core.utils.formatters.NumberFormatter
import com.blockchain.btctransactions.di.scopes.PerApplication
import dagger.Module
import dagger.Provides

@Module
class FormattersModule {

    @Provides
    @PerApplication
    fun dateFormatter(resourceFacade: ResourceFacade): DateFormatter = DateFormatter(resourceFacade)

    @Provides
    @PerApplication
    fun numberFormatter(): NumberFormatter = NumberFormatter()


}