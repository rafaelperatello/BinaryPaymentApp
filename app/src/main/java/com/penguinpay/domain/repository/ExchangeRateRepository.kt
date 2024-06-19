package com.penguinpay.domain.repository

import com.penguinpay.domain.model.ExchangeRate
import com.penguinpay.domain.util.Resource

interface ExchangeRateRepository {

    suspend fun getExchangeRate(symbols: String, baseCurrency: String): Resource<ExchangeRate>
}