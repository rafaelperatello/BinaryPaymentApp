package com.penguinpay.data.exchangerate

import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val EXPIRATION_SECONDS = 5L

/**
 * In-memory cache
 */
interface ExchangeRateLocalDataSource {

    fun getFromCache(): ExchangeRate?

    fun addToCache(exchangeRate: ExchangeRate)
}

class ExchangeRateLocalDataSourceImpl @Inject constructor(
) : ExchangeRateLocalDataSource {

    private var rate: ExchangeRate? = null

    @Synchronized
    override fun getFromCache(): ExchangeRate? {
        return rate?.takeIf { !it.isExpired() }
    }

    @Synchronized
    override fun addToCache(exchangeRate: ExchangeRate) {
        rate = exchangeRate
    }
}

private fun ExchangeRate.isExpired(): Boolean {
    // Todo fix
    return System.currentTimeMillis() > (timestamp + TimeUnit.SECONDS.toMillis(EXPIRATION_SECONDS))
}