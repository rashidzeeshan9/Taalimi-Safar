package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Internship
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InternshipViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _internships = MutableStateFlow<List<Internship>>(emptyList())
    val internships: StateFlow<List<Internship>> = _internships

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // 👇 categoryId is now optional. If null, it fetches ALL internships.
    fun fetchInternships(categoryId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = api.getInternships(categoryId = categoryId)
                _internships.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load internships: ${e.localizedMessage}"
                println("API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}