package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

data class EducationBoard(
    val id: Int,
    val name: String,
    val name_hi: String? = null,
    val name_ur: String? = null,
    val logo: String? = null,
    val website: String? = null,
    val overview: String? = null,
    val overview_hi: String? = null,
    val overview_ur: String? = null,
    val description: String? = null,
    val slug: String,
    val subjects: List<BoardSubject> = emptyList(),

    // 🔥 FIXED: Maps the JSON "exam_patterns" to our newly renamed class
    @SerializedName("exam_patterns")
    val exam_patterns: List<BoardExamPattern> = emptyList(),

    val news: List<BoardNews> = emptyList(),
    val programs: List<SchoolProgram> = emptyList()
)

data class BoardSubject(
    val id: Int,
    val name: String,
    val name_hi: String? = null,
    val name_ur: String? = null,
    val description: String? = null
)

// 🔥 FIXED: Renamed to avoid conflicts, and added missing _hi / _ur fields
data class BoardExamPattern(
    val id: Int,
    val title: String,
    val title_hi: String? = null,
    val title_ur: String? = null,
    val description: String? = null,
    val description_hi: String? = null,
    val description_ur: String? = null
)

data class BoardNews(
    val id: Int,
    val title: String,
    val title_hi: String? = null,
    val title_ur: String? = null,
    val date: String,
    val link: String? = null
)

data class SchoolProgram(
    val id: Int,
    val title: String,
    val title_hi: String? = null,
    val title_ur: String? = null,
    val class_level: String,
    val stream: String? = null,
    val image: String? = null,
    val overview: String? = null,
    val duration: String? = null,
    val eligibility: String? = null,
    val exam_pattern: String? = null,
    val pyqs: List<SchoolPYQ> = emptyList(),

    // 🔥 FIXED: Maps the JSON "platforms" to our newly renamed class
    @SerializedName("platforms")
    val platforms: List<ProgramLearningPlatform> = emptyList(),

    val resources: List<SchoolResource> = emptyList(),
    val faqs: List<SchoolFAQ> = emptyList()
)

data class SchoolPYQ(
    val id: Int,
    val year: String,
    val subject: String,
    val exam_type: String,
    val file: String
)

// 🔥 FIXED: Renamed to avoid conflicts, and added missing _hi / _ur fields
data class ProgramLearningPlatform(
    val id: Int,
    val name: String,
    val description: String? = null,
    val description_hi: String? = null,
    val description_ur: String? = null,
    val app_link: String? = null,
    val website: String? = null
)

data class SchoolResource(
    val id: Int,
    val title: String,
    val file: String
)

data class SchoolFAQ(
    val id: Int,
    val question: String,
    val answer: String
)