package com.penguinpay.data.exchangerate

import com.google.gson.annotations.SerializedName
import com.penguinpay.domain.model.ExchangeRate

data class ExchangeRateEntity(
    @SerializedName("timestamp")
    val timestamp: Long = 0,
    @SerializedName("rates")
    val rates: Map<String, String>
)

fun ExchangeRateEntity.toExchangeRate(): ExchangeRate {
    return ExchangeRate(
        this.timestamp,
        this.rates
    )
}
