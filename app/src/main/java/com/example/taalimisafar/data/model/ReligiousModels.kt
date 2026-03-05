package com.example.taalimisafar.data.model

data class Religion(
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?
)

data class ReligiousProgram(
    val id: Int,
    val religion: Int,
    val program_name: String,
    val program_name_hi: String?,
    val program_name_ur: String?,
    val image: String?,
    val description: String,
    val description_hi: String?,
    val description_ur: String?,
    val sections: List<ProgramSection> = emptyList(),
    val videos: List<ProgramVideo> = emptyList(),
    val references: List<ProgramReference> = emptyList(),
    val faqs: List<ProgramFAQ> = emptyList()
)

data class ProgramSection(
    val id: Int,
    val section_type: String,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val content: String,
    val content_hi: String?,
    val content_ur: String?
)

data class ProgramVideo(
    val id: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val youtube_link: String
)

data class ProgramReference(
    val id: Int,
    val source_name: String?,
    val reference_link: String
)

data class ProgramFAQ(
    val id: Int,
    val question: String,
    val question_hi: String?,
    val question_ur: String?,
    val answer: String,
    val answer_hi: String?,
    val answer_ur: String?
)