package com.penguinpay.domain.model

data class ExchangeRate(
    val timestamp: Long = 0,
    val rates: Map<String, String>
)