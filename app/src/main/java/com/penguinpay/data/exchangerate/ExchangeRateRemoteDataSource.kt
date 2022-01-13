package com.penguinpay.data.exchangerate

import com.penguinpay.data.Constants
import com.penguinpay.data.NetworkResult
import com.penguinpay.data.safeApiCall
import javax.inject.Inject

/**
 * Fetch data from the API
 */
interface ExchangeRateRemoteDataSource {

    suspend fun fetch(symbols: String): NetworkResult<ExchangeRate>
}

class ExchangeRateRemoteDataSourceImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRateRemoteDataSource {

    override suspend fun fetch(symbols: String): NetworkResult<ExchangeRate> {
        return safeApiCall { api.getExchangeRate(Constants.API_KEY, "USD", symbols, true) }
    }
}