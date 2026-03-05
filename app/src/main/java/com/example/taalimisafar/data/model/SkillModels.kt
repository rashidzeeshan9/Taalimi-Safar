package com.example.taalimisafar.data.model

data class SkillProgram(
    val id: Int,
    val category: Int,
    val category_name: String?,
    val image: String?,
    val provider_name: String?,
    val provider_name_hi: String?,
    val provider_name_ur: String?,
    val program_link: String?,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val mode: String,
    val level: String,
    val duration: String,
    val duration_hi: String?,
    val duration_ur: String?,
    val overview: String,
    val overview_hi: String?,
    val overview_ur: String?,
    val eligibility: String?,
    val eligibility_hi: String?,
    val eligibility_ur: String?,
    val fees: String?,
    val placement_support: Boolean,
    val certification_provided: Boolean,
    val modules: List<SkillModule> = emptyList()
)

data class SkillModule(
    val id: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)