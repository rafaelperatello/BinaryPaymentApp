package com.penguinpay.domain

import com.penguinpay.domain.repository.ExchangeRateRepository
import com.penguinpay.domain.util.Resource
import com.penguinpay.util.Country
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.pow


interface GetExchangeRateUseCase {

    suspend fun execute(
        symbols: String,
        baseCurrency: String,
        amount: String,
        country: Country
    ): Resource<String>
}

class GetExchangeRateUseCaseImpl @Inject constructor(
    private val repository: ExchangeRateRepository
) : GetExchangeRateUseCase {

    /**
     * Max allowed to send: 131071 (17 bits)
     * Max exchange rate: VND 22716 (2022-01-13)
     * Estimated max value: 131071 * 22716 = 2977408836 - Fits Long
     */
    override suspend fun execute(
        symbols: String,
        baseCurrency: String,
        amount: String,
        country: Country
    ): Resource<String> {
        return when (val exchange = repository.getExchangeRate(
            symbols = symbols,
            baseCurrency = baseCurrency
        )) {
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