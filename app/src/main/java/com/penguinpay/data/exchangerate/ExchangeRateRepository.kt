package com.penguinpay.data.exchangerate

import android.util.Log
import com.penguinpay.data.Resource
import com.penguinpay.data.toResource
import com.penguinpay.di.CONTEXT_IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

interface ExchangeRateRepository {

    suspend fun getExchangeRate(): Resource<ExchangeRate>
}

class ExchangeRateRepositoryImpl @Inject constructor(
    @Named(CONTEXT_IO) private val ioContext: CoroutineContext,
    private val localDataSource: ExchangeRateLocalDataSource,
    private val remoteDataSource: ExchangeRateRemoteDataSource
) : ExchangeRateRepository {

    override suspend fun getExchangeRate(): Resource<ExchangeRate> = withContext(ioContext) {
        localDataSource.getFromCache()?.let {
            Log.i("getExchangeRate", "cache hit $it")
            Resource.Success(it)
        } ?: remoteDataSource.fetch().toResource().also {
            Log.i("getExchangeRate", "remote call $it")

            if (it is Resource.Success) {
                Log.i("getExchangeRate", "adding to cache ${it.data}")
                localDataSource.addToCache(it.data)
            }
        }
    }
}