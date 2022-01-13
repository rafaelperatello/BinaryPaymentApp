package com.penguinpay.domain

import com.penguinpay.data.Resource
import com.penguinpay.data.exchangerate.ExchangeRateRepository
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.pow


interface ExchangeRateInteractor {

    suspend fun init(): Resource<Unit>

    suspend fun convert(amount: String, country: Country): Resource<String>
}

class ExchangeRateInteractorImpl @Inject constructor(
    private val repository: ExchangeRateRepository,
    private val countryProvider: CountryProvider
) : ExchangeRateInteractor {

    private val symbols by lazy {
        countryProvider.countries.joinToString(",") { it.currency }
    }

    override suspend fun init(): Resource<Unit> {
        return when (repository.getExchangeRate(symbols)) {
            is Resource.ErrorEmptyData,
            is Resource.ErrorNetwork -> Resource.ErrorNetwork()
            is Resource.Success,
            is Resource.SuccessData -> Resource.Success()
        }
    }

    /**
     * Max allowed to send: 131071 (17 bits)
     * Max exchange rate: VND 22716 (2022-01-13)
     * Estimated max value: 131071 * 22716 = 2977408836 - Fits Long
     */
    override suspend fun convert(amount: String, country: Country): Resource<String> {
        return when (val exchange = repository.getExchangeRate(symbols)) {
            is Resource.SuccessData -> {
                val amountToSend = BigDecimal(toDecimal(amount))
                val rate = exchange.data.rates[country.currency]

                if (rate == null) {
                    Resource.ErrorEmptyData()
                } else {
                    val result = amountToSend.times(BigDecimal(rate))
                        .setScale(0, BigDecimal.ROUND_HALF_UP)
                    Resource.SuccessData(toBinary(result.longValueExact()))
                }
            }
            else -> Resource.ErrorEmptyData()
        }
    }

    private fun quadraticPow(exponent: Int) = 2.0.pow(exponent.toDouble()).toInt()

    private fun toDecimal(binaryNumber: String): Int {
        var sum = 0
        binaryNumber.reversed().forEachIndexed { k, v ->
            sum += v.toString().toInt() * quadraticPow(k)
        }
        return sum
    }

    private fun toBinary(decimalNumber: Long, binaryString: String = ""): String {
        while (decimalNumber > 0) {
            val temp = "${binaryString}${decimalNumber % 2}"
            return toBinary(decimalNumber / 2, temp)
        }
        return binaryString.reversed()
    }
}