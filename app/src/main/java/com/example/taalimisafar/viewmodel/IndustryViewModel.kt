package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.IndustryCategory
import com.example.taalimisafar.data.model.IndustryProgram
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IndustryViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {
    private val _categories = MutableStateFlow<List<IndustryCategory>>(emptyList())
    val categories: StateFlow<List<IndustryCategory>> = _categories

    private val _programs = MutableStateFlow<List<IndustryProgram>>(emptyList())
    val programs: StateFlow<List<IndustryProgram>> = _programs

    private val _selectedProgram = MutableStateFlow<IndustryProgram?>(null)
    val selectedProgram: StateFlow<IndustryProgram?> = _selectedProgram

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                _categories.value = api.getIndustryCategories()
            } catch (e: Exception) {
                Log.e("IndustryViewModel", "Category API Error: ${e.message}")
            }
        }
    }

    fun fetchPrograms(
        categoryId: Int? = null,
        demandLevel: String? = null,
        isFeatured: Boolean? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _programs.value = api.getIndustryPrograms(
                    categoryId = categoryId,
                    demandLevel = demandLevel,
                    isFeatured = isFeatured
                )
            } catch (e: Exception) {
                Log.e("IndustryViewModel", "Programs API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProgramDetail(programId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedProgram.value = api.getIndustryProgramDetail(programId)
            } catch (e: Exception) {
                Log.e("IndustryViewModel", "Program Detail API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}