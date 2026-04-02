package com.example.taalimisafar.data.model

data class CommunityQuestion(
    val id: String,
    val author: String,
    val category: String,
    val text: String,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val isFollowing: Boolean = false,
    val userVote: Int = 0, // 1 for up, -1 for down, 0 for none
    val answers: List<CommunityAnswer> = emptyList()
)

data class CommunityAnswer(
    val id: String,
    val author: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
