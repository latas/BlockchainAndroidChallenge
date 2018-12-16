package com.blockchain.btctransactions.core.api

import com.blockchain.btctransactions.data.MultiAddressData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET("multiaddr")
    fun multiAddress(@Query("active") parameter: String): Single<MultiAddressData>
}