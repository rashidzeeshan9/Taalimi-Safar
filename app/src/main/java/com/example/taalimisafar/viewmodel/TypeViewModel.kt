package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.ScholarshipType
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TypeViewModel : ViewModel() {
    private val _types = MutableStateFlow<List<ScholarshipType>>(emptyList())
    val types = _types.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchTypes()
    }

    private fun fetchTypes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitClient.api.getScholarshipTypes()
                _types.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}