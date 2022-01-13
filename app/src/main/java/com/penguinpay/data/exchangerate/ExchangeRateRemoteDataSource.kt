package com.penguinpay.data.exchangerate

import com.penguinpay.data.NetworkResult
import com.penguinpay.data.safeApiCall
import javax.inject.Inject

/**
 * Fetch data from the API
 */
interface ExchangeRateRemoteDataSource {

    suspend fun fetch(): NetworkResult<ExchangeRate>
}

class ExchangeRateRemoteDataSourceImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRateRemoteDataSource {

    override suspend fun fetch(): NetworkResult<ExchangeRate> {
        return safeApiCall { api.getExchangeRate() }
    }

//    override suspend fun fetch(): NetworkResult<ExchangeRate> {
//        return NetworkResult.Success(ExchangeRate(System.currentTimeMillis(), emptyMap()))
//    }
}