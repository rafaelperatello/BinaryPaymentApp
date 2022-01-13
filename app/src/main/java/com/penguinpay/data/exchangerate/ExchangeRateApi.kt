package com.penguinpay.data.exchangerate

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRateApi {

    // Todo update path
    @GET("2018/01/22/life-as-an-android-engineer")
    suspend fun getExchangeRate(): Response<ExchangeRate>
}

data class ExchangeRate(
    @SerializedName("timestamp")
    val timestamp: Long = 0,
    @SerializedName("rates")
    val rates: Map<String, String>
)