package com.penguinpay.data.exchangerate

import android.util.Log
import com.penguinpay.data.exchangerate.local.ExchangeRateLocalDataSource
import com.penguinpay.data.exchangerate.remote.ExchangeRateRemoteDataSource
import com.penguinpay.data.toResource
import com.penguinpay.di.CONTEXT_IO
import com.penguinpay.domain.model.ExchangeRate
import com.penguinpay.domain.repository.ExchangeRateRepository
import com.penguinpay.domain.util.Resource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class ExchangeRateRepositoryImpl @Inject constructor(
    @Named(CONTEXT_IO) private val ioContext: CoroutineContext,
    private val localDataSource: ExchangeRateLocalDataSource,
    private val remoteDataSource: ExchangeRateRemoteDataSource
) : ExchangeRateRepository {

    override suspend fun getExchangeRate(
        symbols: String,
        baseCurrency: String
    ): Resource<ExchangeRate> =
        withContext(ioContext) {
            localDataSource.getFromCache()?.let {
                Log.i("getExchangeRate", "cache hit $it")
                Resource.SuccessData(it)
            } ?: remoteDataSource.fetch(symbols, baseCurrency)
                .toResource {
                    this.toExchangeRate()
                }
                .also {
                    Log.i("getExchangeRate", "remote call $it")

                    if (it is Resource.SuccessData) {
                        Log.i("getExchangeRate", "adding to cache ${it.data}")
                        localDataSource.addToCache(it.data)
                    }
                }
        }
}