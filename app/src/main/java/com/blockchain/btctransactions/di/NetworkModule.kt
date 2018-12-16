package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.BuildConfig
import com.blockchain.btctransactions.di.qualifiers.BaseUrl
import com.blockchain.btctransactions.di.scopes.PerApplication
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
class NetworkModule {

    @PerApplication
    @Provides
    fun provideApiService(
        @BaseUrl baseUrl: String, client: OkHttpClient): GameApi {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(
                RxJava2ReauthCallAdapterFactory.create(
                    reauthFlowableProvider,
                    { it is HttpException && it.code() == 401 },
                    3
                )
            )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GameApi::class.java)
    }

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}