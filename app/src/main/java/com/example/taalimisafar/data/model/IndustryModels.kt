package com.example.taalimisafar.data.model

data class IndustryCategory(
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val link: String?,
    val icon: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)

data class IndustryProgram(
    val id: Int,
    val category: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val link: String?,
    val demand_level: String?,
    val image: String?,
    val is_featured: Boolean,
    val overview: String?,
    val overview_hi: String?,
    val overview_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?,
    val duration: String?,
    val duration_hi: String?,
    val duration_ur: String?,
    val eligibility: String?,
    val eligibility_hi: String?,
    val eligibility_ur: String?,
    val future_scope: String?,
    val future_scope_hi: String?,
    val future_scope_ur: String?,
    val average_salary: String?,
    val average_salary_hi: String?,
    val average_salary_ur: String?,

    // Nested Lists matching the Django Serializer!
    val career_paths: List<CareerPath> = emptyList(),
    val roadmaps: List<CareerRoadmap> = emptyList(),
    val skills: List<IndustrySkill> = emptyList(),
    val tools: List<IndustryTool> = emptyList(),
    val platforms: List<LearningPlatform> = emptyList(),
    val hiring_cities: List<HiringCity> = emptyList(),
    val resources: List<IndustryResource> = emptyList(),
    val faqs: List<IndustryFAQ> = emptyList()
)

data class CareerPath(
    val id: Int, val title: String, val title_hi: String?, val title_ur: String?,
    val description: String?, val description_hi: String?, val description_ur: String?,
    val salary_range: String?
)

data class CareerRoadmap(
    val id: Int, val step_number: Int, val title: String, val title_hi: String?, val title_ur: String?,
    val description: String?, val description_hi: String?, val description_ur: String?,
    val video_link: String?, val website_link: String?
)

data class IndustrySkill(
    val id: Int, val name: String, val name_hi: String?, val name_ur: String?,
    val description: String?, val description_hi: String?, val description_ur: String?
)

data class IndustryTool(
    val id: Int, val name: String, val name_hi: String?, val name_ur: String?,
    val description: String?, val description_hi: String?, val description_ur: String?
)

data class LearningPlatform(
    val id: Int, val name: String, val description: String?, val description_hi: String?,
    val description_ur: String?, val video_link: String?, val website: String?
)

data class HiringCity(
    val id: Int, val city: String, val country: String?
)

data class IndustryResource(
    val id: Int, val title: String, val file: String
)

data class IndustryFAQ(
    val id: Int, val question: String, val question_hi: String?, val question_ur: String?,
    val answer: String, val answer_hi: String?, val answer_ur: String?
)