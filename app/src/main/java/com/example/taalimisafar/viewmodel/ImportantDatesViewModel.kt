package com.example.taalimisafar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.ImportantProgram
import com.example.taalimisafar.data.model.ImportantDate
import com.example.taalimisafar.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImportantDatesViewModel : ViewModel() {
    private val api = RetrofitClient.api

    private val _programs = MutableStateFlow<List<ImportantProgram>>(emptyList())
    val programs: StateFlow<List<ImportantProgram>> = _programs

    private val _selectedProgram = MutableStateFlow<ImportantProgram?>(null)
    val selectedProgram: StateFlow<ImportantProgram?> = _selectedProgram

    private val _upcomingDates = MutableStateFlow<List<ImportantDate>>(emptyList())
    val upcomingDates: StateFlow<List<ImportantDate>> = _upcomingDates

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchPrograms(categoryId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try { _programs.value = api.getImportantPrograms(categoryId) }
            catch (e: Exception) { }
            finally { _isLoading.value = false }
        }
    }

    fun fetchProgramDetail(programId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedProgram.value = api.getImportantProgramDetail(programId)
            } catch (e: Exception) {
                Log.e("ImportantViewModel", "Detail Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun fetchUpcomingDates(timeframe: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try { _upcomingDates.value = api.getUpcomingDates(timeframe) }
            catch (e: Exception) { }
            finally { _isLoading.value = false }
        }
    }
}