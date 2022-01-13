package com.penguinpay.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.penguinpay.R
import com.penguinpay.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.init()

        // Setup countries list
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item_dropdown,
            model.countryNames
        )
        with(binding.textFieldCountry.editText as AutoCompleteTextView) {
            setAdapter(adapter)
            setOnItemClickListener { parent, view, position, id ->
                model.onCountrySelected(position)
            }
        }

        // Todo fix changing converted value when navigating by tab

        lifecycleScope.launch {
            model.timeState.collectLatest {
                //                binding.text.text = it.toString()
            }
        }
    }
}

//        passwordLayout.error = getString(R.string.error)
//        // Clear error text
//        passwordLayout.error = null