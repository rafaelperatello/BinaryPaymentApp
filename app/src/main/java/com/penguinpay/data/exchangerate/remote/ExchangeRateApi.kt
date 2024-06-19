package com.penguinpay.data.exchangerate.remote

import com.penguinpay.data.exchangerate.ExchangeRateEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {

    @GET("api/latest.json")
    suspend fun getExchangeRate(
        @Query("app_id") apiKey: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String,
        @Query("prettyprint") prettyPrint: Boolean
    ): Response<ExchangeRateEntity>
}