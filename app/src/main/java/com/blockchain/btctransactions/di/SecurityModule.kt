package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.di.qualifiers.Xpub
import dagger.Module
import dagger.Provides

@Module
class SecurityModule {
    @Provides
    @Xpub
    fun providesXpub(): String =
        "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"
}