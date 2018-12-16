package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.domain.GetWalletInfoUseCase
import com.blockchain.btctransactions.domain.GetWallet
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun providesGetWalletsUseCase(): GetWalletInfoUseCase =
            GetWallet()

}