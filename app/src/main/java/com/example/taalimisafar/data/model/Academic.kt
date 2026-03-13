package com.example.taalimisafar.data.model

// ----------------------------
// STREAM MODEL
// ----------------------------

data class Stream(
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val image: String?,
    val courses: List<Course> = emptyList()
)


// ----------------------------
// COURSE CURRICULUM MODEL
// ----------------------------

data class CourseCurriculum(
    val id: Int,
    val title: String,
    val description: String
)