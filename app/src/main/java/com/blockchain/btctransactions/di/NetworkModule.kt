package com.blockchain.btctransactions.di

import com.blockchain.btctransactions.BuildConfig
import com.blockchain.btctransactions.core.api.AppService
import com.blockchain.btctransactions.di.qualifiers.BaseUrl
import com.blockchain.btctransactions.di.scopes.PerApplication
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @PerApplication
    @Provides
    fun provideApiService(
        @BaseUrl baseUrl: String, client: OkHttpClient
    ): AppService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppService::class.java)


    @PerApplication
    @Provides
    fun providesHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()


    @Provides
    @BaseUrl
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}