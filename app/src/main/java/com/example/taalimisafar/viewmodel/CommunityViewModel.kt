package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taalimisafar.data.model.CommunityQuestion
import com.example.taalimisafar.data.model.SubmitQuestionRequest
import com.example.taalimisafar.data.model.VoteRequest
import com.example.taalimisafar.data.remote.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityViewModel(private val apiService: ApiService) : ViewModel() {

    private val _allQuestions = MutableStateFlow<List<CommunityQuestion>>(emptyList())
    val questions: StateFlow<List<CommunityQuestion>> = _allQuestions.asStateFlow()

    // Translates UI text to Database IDs
    private val categoryMap = mapOf(
        "Education" to 1, "Skills" to 2, "Career" to 3,
        "Govt. Job" to 4, "Exam Date" to 5, "Business" to 6
    )

    fun fetchCommunityFeed(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getCommunityFeed()
                _allQuestions.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addQuestion(token: String, category: String, text: String, target: String) {
        viewModelScope.launch {
            try {
                val categoryId = categoryMap[category] ?: 1
                val request = SubmitQuestionRequest(
                    target = target,
                    categoryId = categoryId,
                    text = text
                )
                apiService.submitQuestion(request)
                fetchCommunityFeed(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun postAnswer(token: String, questionId: Int, text: String) {
        viewModelScope.launch {
            try {
                val answerData = mapOf("text" to text)
                // Convert Int to String for Retrofit URL path
                apiService.addAnswer(questionId.toString(), answerData)
                fetchCommunityFeed(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleUpvote(token: String, questionId: Int) {
        viewModelScope.launch {
            try {
                val question = _allQuestions.value.find { it.id == questionId } ?: return@launch
                val newVoteValue = if (question.userVote == 1) 0 else 1
                updateLocalVote(questionId, newVoteValue)
                apiService.toggleVote(questionId.toString(), VoteRequest(newVoteValue))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleDownvote(token: String, questionId: Int) {
        viewModelScope.launch {
            try {
                val question = _allQuestions.value.find { it.id == questionId } ?: return@launch
                val newVoteValue = if (question.userVote == -1) 0 else -1
                updateLocalVote(questionId, newVoteValue)
                apiService.toggleVote(questionId.toString(), VoteRequest(newVoteValue))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateLocalVote(questionId: Int, voteValue: Int) {
        _allQuestions.update { currentList ->
            currentList.map { q ->
                if (q.id == questionId) {
                    val oldVote = q.userVote
                    var newUp = q.upvotes
                    var newDown = q.downvotes
                    if (oldVote == 1) newUp-- else if (oldVote == -1) newDown--
                    if (voteValue == 1) newUp++ else if (voteValue == -1) newDown++
                    q.copy(userVote = voteValue, upvotes = newUp, downvotes = newDown)
                } else q
            }
        }
    }

    fun getQuestionById(questionId: Int): CommunityQuestion? {
        return _allQuestions.value.find { it.id == questionId }
    }
}