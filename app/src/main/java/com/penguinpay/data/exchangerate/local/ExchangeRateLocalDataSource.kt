package com.penguinpay.data.exchangerate.local

import com.penguinpay.domain.model.ExchangeRate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val EXPIRATION_MINUTES = 1L

/**
 * In-memory cache
 */
interface ExchangeRateLocalDataSource {

    fun getFromCache(): ExchangeRate?

    fun addToCache(exchangeRate: ExchangeRate)
}

class ExchangeRateLocalDataSourceImpl @Inject constructor() : ExchangeRateLocalDataSource {

    private var rate: ExchangeRate? = null
    private var cacheTimestamp = 0L

    @Synchronized
    override fun getFromCache(): ExchangeRate? {
        return rate?.takeIf { !isCacheExpired() }
    }

    @Synchronized
    override fun addToCache(exchangeRate: ExchangeRate) {
        rate = exchangeRate
        cacheTimestamp = System.currentTimeMillis()
    }

    private fun isCacheExpired(): Boolean {
        return System.currentTimeMillis() > (cacheTimestamp + TimeUnit.MINUTES.toMillis(EXPIRATION_MINUTES))
    }
}