package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

// --- DATA COMING FROM BACKEND ---
data class CommunityQuestion(
    @SerializedName("id") val id: Int, // ✅ Must be Int
    @SerializedName("author") val author: String?,
    @SerializedName("author_profile_pic") val authorProfilePic: String? = null,
    @SerializedName("target") val target: String? = "Everyone",
    @SerializedName("category_name") val category: String?,
    @SerializedName("text_en") val text: String?,
    @SerializedName("upvotes") val upvotes: Int = 0,
    @SerializedName("downvotes") val downvotes: Int = 0,
    @SerializedName("user_vote") val userVote: Int = 0,
    @SerializedName("is_approved") val isApproved: Boolean = false,
    @SerializedName("answers") val answers: List<CommunityAnswer>? = emptyList(),
    @SerializedName("created_at") val createdAt: String? = null
)

data class CommunityAnswer(
    @SerializedName("id") val id: Int, // ✅ Must be Int
    @SerializedName("author") val author: String?,
    @SerializedName("author_profile_pic") val authorProfilePic: String? = null,
    @SerializedName("text") val text: String?,
    @SerializedName("is_admin") val isAdmin: Boolean = false,
    @SerializedName("created_at") val createdAt: String? = null
)

// --- DATA GOING TO BACKEND ---
data class SubmitQuestionRequest(
    @SerializedName("submission_type") val target: String,
    @SerializedName("category") val categoryId: Int, // ✅ Must be Int for Django ForeignKey
    @SerializedName("text_en") val text: String
)

data class VoteRequest(
    @SerializedName("value") val value: Int // ✅ Matches Django's "value" field
)