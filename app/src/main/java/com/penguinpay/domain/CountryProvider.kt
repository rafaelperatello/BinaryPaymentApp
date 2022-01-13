package com.penguinpay.domain

import java.util.*
import javax.inject.Inject

object CountyCodes {
    const val KENYA = "KE"
    const val NIGERIA = "NG"
    const val TANZANIA = "TZ"
    const val UGANDA = "UG"
}

data class Country(
    val locale: Locale,
    val currency: String
) {
    val name: String = locale.displayName
}

interface CountryProvider {

    val countries: List<Country>
}

class CountryProviderImpl @Inject constructor() : CountryProvider {

    override val countries: List<Country> by lazy {
        arrayListOf(
            getCountry(CountyCodes.KENYA),
            getCountry(CountyCodes.NIGERIA),
            getCountry(CountyCodes.TANZANIA),
            getCountry(CountyCodes.UGANDA)
        )
    }

    private fun getCountry(countryCode: String): Country {
        val locale = Locale("", countryCode)
        return Country(locale, Currency.getInstance(locale).currencyCode)
    }
}
