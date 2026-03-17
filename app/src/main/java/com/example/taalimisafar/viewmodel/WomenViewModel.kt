package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.WomenCategory
import com.example.taalimisafar.data.model.WomenProgram
import com.example.taalimisafar.data.remote.ApiService
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WomenViewModel(
    private val api: ApiService = RetrofitClient.api
) : ViewModel() {

    private val _categories = MutableStateFlow<List<WomenCategory>>(emptyList())
    val categories: StateFlow<List<WomenCategory>> = _categories

    private val _programs = MutableStateFlow<List<WomenProgram>>(emptyList())
    val programs: StateFlow<List<WomenProgram>> = _programs

    private val _selectedProgram = MutableStateFlow<WomenProgram?>(null)
    val selectedProgram: StateFlow<WomenProgram?> = _selectedProgram

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categories.value = api.getWomenCategories()
            } catch (e: Exception) {
                Log.e("WomenViewModel", "Categories API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPrograms(categoryId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _programs.value = api.getWomenPrograms(categoryId = categoryId)
            } catch (e: Exception) {
                Log.e("WomenViewModel", "Programs API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchProgramDetail(programId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedProgram.value = api.getWomenProgramDetail(programId)
            } catch (e: Exception) {
                Log.e("WomenViewModel", "Program Detail API Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}