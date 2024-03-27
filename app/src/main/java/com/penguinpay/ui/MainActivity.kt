package com.penguinpay.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.penguinpay.R
import com.penguinpay.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val inputFields by lazy {
        mapOf(
            MainViewModel.INPUT_COUNTRY.first to binding.textFieldCountry,
            MainViewModel.INPUT_FIRST_NAME.first to binding.textFieldName,
            MainViewModel.INPUT_LAST_NAME.first to binding.textFieldLastName,
            MainViewModel.INPUT_PHONE.first to binding.textFieldPhone,
            MainViewModel.INPUT_AMOUNT.first to binding.textFieldAmount
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.init()

        // Setup countries list
        val countriesAdapter = ArrayAdapter(this, R.layout.list_item_dropdown, model.countryNames)
        with(binding.textFieldCountry.editText as AutoCompleteTextView) {
            setAdapter(countriesAdapter)
            setText(countriesAdapter.getItem(model.countrySelectedPosition).toString(), false)
            setOnItemClickListener { _, _, position, _ ->
                model.onCountrySelected(position)
            }
        }

        // Clicks
        with(binding) {
            buttonSend.setOnClickListener {
                model.onSendClick(
                    textFieldName.editText?.text?.toString() ?: "",
                    textFieldLastName.editText?.text?.toString() ?: "",
                    textFieldPhone.editText?.text?.toString() ?: "",
                    textFieldAmount.editText?.text?.toString() ?: "",
                )
            }

            textFieldAmount.editText?.addTextChangedListener {
                model.onAmountChanged(it.toString())
            }

            textFieldAmount.editText?.doAfterTextChanged {
                it?.toString()?.let { text ->
                    if (text.contains(".")) {
                        textFieldAmount.editText?.let { editText ->
                            val newText = text.replace(".", "")
                            editText.setText(
                                newText,
                                TextView.BufferType.EDITABLE
                            )
                            editText.setSelection(newText.length)
                        }
                    }
                }
            }
        }

        // State listener
        lifecycleScope.launch {
            model.viewState.collect {
                toggleLoading(false)
                when (it) {
                    MainViewModel.MainViewModelState.Loading -> toggleLoading(true)
                    MainViewModel.MainViewModelState.InternetError -> showInternetError()
                    MainViewModel.MainViewModelState.Ready -> Unit
                    MainViewModel.MainViewModelState.ValidFields -> clearErrors()
                    is MainViewModel.MainViewModelState.InvalidFields -> showErrors(it.fields)
                    MainViewModel.MainViewModelState.Success -> {
                        clearErrors()
                        showSuccess()
                    }

                }
            }
        }

        // Amount-to-receive listener
        lifecycleScope.launch {
            model.amountToReceive.collect {
                binding.textViewConvertedAmount.editText?.setText(it, TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun toggleLoading(visible: Boolean) {
        binding.loadingGroup.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showInternetError() {
        binding.loadingOverlay.visibility = View.VISIBLE
        Snackbar.make(binding.root, R.string.error_network, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_retry) { _ -> model.onRetry() }
            .show()
    }

    private fun clearErrors() {
        inputFields.onEach {
            it.value.hideError()
        }
    }

    private fun showErrors(fields: List<Pair<String, Int>>) {
        clearErrors()

        fields.map { inputFields[it.first] to it.second }
            .onEach {
                it.first?.showError(it.second)
            }
    }

    private fun showSuccess() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.result_success_title)
            .setMessage(R.string.result_success_text)
            .setPositiveButton(R.string.button_ok, null)
            .setOnDismissListener { model.onDialogClosed() }
            .setCancelable(true)
            .show()
    }

    private fun TextInputLayout.showError(errorLabel: Int) {
        error = getString(errorLabel)
    }

    private fun TextInputLayout.hideError() {
        error = null
        isErrorEnabled = false
    }
}