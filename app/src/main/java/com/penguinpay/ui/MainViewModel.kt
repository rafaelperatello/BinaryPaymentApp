package com.penguinpay.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.penguinpay.R
import com.penguinpay.domain.GetExchangeRateUseCase
import com.penguinpay.domain.repository.ExchangeRateRepository
import com.penguinpay.domain.util.Resource
import com.penguinpay.util.Country
import com.penguinpay.util.CountryProvider
import com.penguinpay.util.PhoneNumberHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal const val BASE_CURRENCY = "USD"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val phoneNumberHelper: PhoneNumberHelper,
    private val countryProvider: CountryProvider
) : ViewModel() {

    sealed class MainViewModelState {
        data object Loading : MainViewModelState()
        data object Ready : MainViewModelState()
        data object InternetError : MainViewModelState()
        data object ValidFields : MainViewModelState()
        class InvalidFields(val fields: List<Pair<String, Int>>) : MainViewModelState()
        data object Success : MainViewModelState()
    }

    companion object {
        val INPUT_COUNTRY = "INPUT_COUNTRY" to R.string.field_error_country
        val INPUT_FIRST_NAME = "INPUT_FIRST_NAME" to R.string.field_error_first_name
        val INPUT_LAST_NAME = "INPUT_LAST_NAME" to R.string.field_error_last_name
        val INPUT_PHONE = "INPUT_PHONE" to R.string.field_error_phone
        val INPUT_AMOUNT = "INPUT_AMOUNT" to R.string.field_error_amount
    }

    private val _viewState = MutableStateFlow<MainViewModelState>(MainViewModelState.Loading)
    val viewState: StateFlow<MainViewModelState> = _viewState

    private val _amountToReceive = MutableStateFlow("")
    val amountToReceive: StateFlow<String> = _amountToReceive

    private val amountToSend = MutableStateFlow("")

    private val selectedCountry: Country
        get() = countryProvider.countries[selectedPositionInternal]

    private var selectedPositionInternal = 0
    val countrySelectedPosition: Int
        get() = selectedPositionInternal

    val countryNames: List<String> by lazy {
        countryProvider.countries.map { it.name }
    }

    private val symbols by lazy {
        countryProvider.countries.joinToString(",") { it.currency }
    }

    fun init() {
        fetchFromApi()
        listenChangesToAmount()
    }

    fun onCountrySelected(position: Int) {
        selectedPositionInternal = position
        Log.i("onCountrySelected", countryProvider.countries[position].name)

        val currentAmount = amountToSend.value
        if (currentAmount.isBlank()) {
            // Only update currency
            updateAmountToReceive("")
        } else {
            // Trigger update to convert the value
            amountToSend.value = ""
            amountToSend.value = currentAmount
        }
    }

    fun onAmountChanged(amount: String) {
        amountToSend.value = amount
    }

    fun onSendClick(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        amount: String
    ) {
        val fields = mutableListOf<Pair<String, Int>>()

        if (firstName.isBlank()) {
            fields.add(INPUT_FIRST_NAME)
        }

        if (lastName.isBlank()) {
            fields.add(INPUT_LAST_NAME)
        }

        if (phoneNumber.isBlank() || !phoneNumberHelper.isNumberValid(
                phoneNumber,
                selectedCountry
            )
        ) {
            fields.add(INPUT_PHONE)
        }

        val containsZeroOnly = amount.fold(true) { acc, c ->
            acc && c == '0'
        }

        if (amount.isBlank() || containsZeroOnly) {
            fields.add(INPUT_AMOUNT)
        }

        if (fields.isEmpty()) {
            _viewState.value = MainViewModelState.Success
        } else {
            _viewState.value = MainViewModelState.InvalidFields(fields)
        }
    }

    fun onDialogClosed() {
        _viewState.value = MainViewModelState.Ready
    }

    fun onRetry() {
        fetchFromApi()
    }

    private fun fetchFromApi() {
        _viewState.value = MainViewModelState.Loading

        viewModelScope.launch {
            _viewState.value =
                when (exchangeRateRepository.getExchangeRate(symbols, BASE_CURRENCY)) {
                    is Resource.ErrorEmptyData,
                    is Resource.ErrorNetwork -> MainViewModelState.InternetError

                    is Resource.Success,
                    is Resource.SuccessData -> MainViewModelState.Ready
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun listenChangesToAmount() {
        viewModelScope.launch {
            amountToSend
                .debounce(200.milliseconds)
                .collectLatest { amount ->
                    val value = if (amount.isBlank()) {
                        ""
                    } else {
                        (getExchangeRateUseCase.execute(
                            symbols = symbols,
                            baseCurrency = BASE_CURRENCY,
                            amount = amount,
                            country = selectedCountry
                        ) as? Resource.SuccessData)
                            ?.data
                            ?: ""
                    }
                    updateAmountToReceive(value)
                }
        }
    }

    private fun updateAmountToReceive(value: String) {
        val countryCurrency = selectedCountry.currency
        _amountToReceive.value = "$countryCurrency $value"
    }
}