package com.example.taalimisafar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taalimisafar.data.model.CommunityAnswer
import com.example.taalimisafar.data.model.CommunityQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CommunityViewModel : ViewModel() {
    private val _questions = MutableStateFlow<List<CommunityQuestion>>(emptyList())
    val questions: StateFlow<List<CommunityQuestion>> = _questions.asStateFlow()

    fun addQuestion(author: String, category: String, text: String) {
        val newQuestion = CommunityQuestion(
            id = UUID.randomUUID().toString(),
            author = author,
            category = category,
            text = text
        )
        _questions.update { currentList -> listOf(newQuestion) + currentList }
    }

    fun toggleUpvote(questionId: String) {
        _questions.update { currentList ->
            currentList.map { q ->
                if (q.id == questionId) {
                    when (q.userVote) {
                        1 -> q.copy(userVote = 0, upvotes = q.upvotes - 1)
                        -1 -> q.copy(userVote = 1, upvotes = q.upvotes + 1, downvotes = q.downvotes - 1)
                        else -> q.copy(userVote = 1, upvotes = q.upvotes + 1)
                    }
                } else q
            }
        }
    }

    fun toggleDownvote(questionId: String) {
        _questions.update { currentList ->
            currentList.map { q ->
                if (q.id == questionId) {
                    when (q.userVote) {
                        -1 -> q.copy(userVote = 0, downvotes = q.downvotes - 1)
                        1 -> q.copy(userVote = -1, downvotes = q.downvotes + 1, upvotes = q.upvotes - 1)
                        else -> q.copy(userVote = -1, downvotes = q.downvotes + 1)
                    }
                } else q
            }
        }
    }

    fun addAnswer(questionId: String, author: String, text: String) {
        val newAnswer = CommunityAnswer(
            id = UUID.randomUUID().toString(),
            author = author,
            text = text
        )
        _questions.update { currentList ->
            currentList.map { q ->
                if (q.id == questionId) {
                    q.copy(answers = q.answers + newAnswer)
                } else q
            }
        }
    }
    
    fun getQuestionById(questionId: String): CommunityQuestion? {
        return _questions.value.find { it.id == questionId }
    }
}
