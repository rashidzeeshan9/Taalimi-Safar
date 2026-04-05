package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.EducationBoard
import com.example.taalimisafar.data.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EducationViewModel(private val apiService: ApiService) : ViewModel() {
    private val _boards = MutableStateFlow<List<EducationBoard>>(emptyList())
    val boards: StateFlow<List<EducationBoard>> = _boards

    private val _selectedBoard = MutableStateFlow<EducationBoard?>(null)
    val selectedBoard: StateFlow<EducationBoard?> = _selectedBoard

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchBoards() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getEducationBoards()
                if (response.isSuccessful) {
                    _boards.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchBoardDetail(boardId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getBoardDetail(boardId)
                if (response.isSuccessful) {
                    _selectedBoard.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}