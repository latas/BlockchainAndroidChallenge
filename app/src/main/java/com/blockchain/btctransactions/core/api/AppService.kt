package com.blockchain.btctransactions.core.api

import androidx.lifecycle.LiveData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET
    fun getUser(@Query("active") parameter: String): Single<MultiAddressResponse>
}