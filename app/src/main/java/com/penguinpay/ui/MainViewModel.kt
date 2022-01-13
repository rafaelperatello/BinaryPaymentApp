package com.penguinpay.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penguinpay.data.exchangerate.ExchangeRateRepository
import com.penguinpay.domain.Country
import com.penguinpay.domain.CountryProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ExchangeRateRepository,
    private val countryProvider: CountryProvider
) : ViewModel() {

    private val timeStateInternal = MutableStateFlow(0L)
    val timeState: StateFlow<Long> = timeStateInternal

    private var selectedCountry: Country? = null
    val countryNames: List<String> by lazy {
        countryProvider.countries.map { it.name }
    }

    fun init() {
        viewModelScope.launch {

        }
    }

    fun onCountrySelected(position: Int) {
        selectedCountry = countryProvider.countries[position].also {
            Log.i("onCountrySelected", it.name)
        }
    }

    fun onSendClick(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        amount: String
    ) {

    }

    //    when (val rate = repository.getExchangeRate()) {
    //        is Resource.Success -> timeStateInternal.value = rate.data.timestamp
    //    }

}