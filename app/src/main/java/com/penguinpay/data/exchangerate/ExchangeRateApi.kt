package com.penguinpay.data.exchangerate

import com.google.gson.annotations.SerializedName
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
    ): Response<ExchangeRate>
}

data class ExchangeRate(
    @SerializedName("timestamp")
    val timestamp: Long = 0,
    @SerializedName("rates")
    val rates: Map<String, String>
)