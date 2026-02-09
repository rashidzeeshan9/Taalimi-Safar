package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.remote.RetrofitClient
import com.example.taalimisafar.data.model.Scholarship
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScholarshipViewModel : ViewModel() {

    // 1. State to hold the list of scholarships
    private val _scholarships = MutableStateFlow<List<Scholarship>>(emptyList())
    val scholarships = _scholarships.asStateFlow()

    // 2. State to hold errors (optional but good for debugging)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        // Fetch data immediately when ViewModel is created
        fetchScholarships()
    }

    fun fetchScholarships() {
        viewModelScope.launch {
            try {
                // Call the API
                val response = RetrofitClient.api.getScholarships()
                _scholarships.value = response
                _errorMessage.value = null // Clear errors if successful
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to load: ${e.message}"
            }
        }
    }
}