package com.example.taalimisafar.data.model

data class DiplomaProgram(
    val id: Int,
    val category: Int,
    val category_name: String?,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val thumbnail: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?,
    val eligibility: String?,
    val eligibility_hi: String?,
    val eligibility_ur: String?,
    val admission_process: String?,
    val admission_process_hi: String?,
    val admission_process_ur: String?,
    val career_opportunities: String?,
    val career_opportunities_hi: String?,
    val career_opportunities_ur: String?,
    val duration: String?,
    val fees: String?,
    val institute_name: String?,
    val institute_city: String?,
    val institute_state: String?,
    val institute_website: String?,
    val apply_link: String?,
    val resources: List<DiplomaResource> = emptyList(),
    val institutes: List<DiplomaInstitute> = emptyList()
)

data class DiplomaResource(
    val id: Int,
    val title: String,
    val file: String?,
    val link: String?
)

data class DiplomaInstitute(
    val id: Int,
    val institute_name: String,
    val institute_city: String,
    val institute_state: String,
    val institute_website: String?
)