package com.blockchain.btctransactions.api

import com.blockchain.btctransactions.core.api.AppService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Okio
import org.junit.After
import org.junit.Before
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class ApiTest {

    protected lateinit var service: AppService
    private lateinit var mockWebServer: MockWebServer

    @Before
    open fun setUp() {
        mockWebServer = MockWebServer()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.MILLISECONDS)
            .readTimeout(100, TimeUnit.MILLISECONDS)
            .writeTimeout(100, TimeUnit.MILLISECONDS)
            .build()

        service = Mockito.spy(
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AppService::class.java)
        )
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(fileName: String, responseCode: Int = 200, noResponse: Boolean = false) {

        val inputStream = javaClass.classLoader
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        if (noResponse) {
            mockResponse.socketPolicy = SocketPolicy.NO_RESPONSE
        }

        mockWebServer.enqueue(
            mockResponse.setResponseCode(responseCode)
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}