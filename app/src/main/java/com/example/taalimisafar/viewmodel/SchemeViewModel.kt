package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.Schemes
import com.example.taalimisafar.data.model.SchemesCategory
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SchemeViewModel(
    private val api: ApiService = RetrofitClient.api // Matches ReligiousViewModel
) : ViewModel() {

    private val _categories = MutableStateFlow<List<SchemesCategory>>(emptyList())
    val categories: StateFlow<List<SchemesCategory>> = _categories

    private val _schemes = MutableStateFlow<List<Schemes>>(emptyList())
    val schemes: StateFlow<List<Schemes>> = _schemes

    private val _selectedScheme = MutableStateFlow<Schemes?>(null)
    val selectedScheme: StateFlow<Schemes?> = _selectedScheme

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                _categories.value = api.getSchemeCategories()
            } catch (e: Exception) {
                Log.e("SchemeViewModel", "Category API Error: ${e.message}")
            }
        }
    }

    // Update this function to accept BOTH categoryId and status
    fun fetchSchemes(categoryId: Int? = null, status: String? = "Active") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Pass the status to the API
                _schemes.value = api.getSchemes(categoryId = categoryId, status = status)
            } catch (e: Exception) {
                Log.e("SchemeViewModel", "Schemes API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun fetchSchemeDetail(schemeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedScheme.value = api.getSchemeDetail(schemeId)
            } catch (e: Exception) {
                Log.e("SchemeViewModel", "Detail API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}