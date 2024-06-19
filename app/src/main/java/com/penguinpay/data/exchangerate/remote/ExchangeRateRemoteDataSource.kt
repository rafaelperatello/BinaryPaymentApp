package com.penguinpay.data.exchangerate.remote

import com.penguinpay.data.Constants
import com.penguinpay.data.NetworkResult
import com.penguinpay.data.exchangerate.ExchangeRateEntity
import com.penguinpay.data.safeApiCall
import javax.inject.Inject

/**
 * Fetch data from the API
 */
interface ExchangeRateRemoteDataSource {

    suspend fun fetch(symbols: String, baseCurrency: String): NetworkResult<ExchangeRateEntity>
}

class ExchangeRateRemoteDataSourceImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRateRemoteDataSource {

    override suspend fun fetch(
        symbols: String,
        baseCurrency: String
    ): NetworkResult<ExchangeRateEntity> {
        return safeApiCall {
            api.getExchangeRate(
                apiKey = Constants.API_KEY,
                base = baseCurrency,
                symbols = symbols,
                prettyPrint = true
            )
        }
    }
}