package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val _quoteState = MutableStateFlow<Quote?>(null)
    val quoteState = _quoteState.asStateFlow()

    init {
        fetchQuote()
    }

    private fun fetchQuote() {
        viewModelScope.launch {
            try {
                Log.d("API_TEST", "Attempting to connect to: http://10.0.2.2:8000/api/quotes/")
                val response = RetrofitClient.api.getQuotes()

                Log.d("API_TEST", "Response received. Items: ${response.size}")

                if (response.isNotEmpty()) {
                    _quoteState.value = response[0]
                } else {
                    Log.e("API_TEST", "Error: The list is EMPTY in Django!")
                }

            } catch (e: Exception) {

                Log.e("API_TEST", "CRITICAL ERROR: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}