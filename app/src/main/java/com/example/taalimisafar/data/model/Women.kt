package com.example.taalimisafar.data.model

import com.google.crypto.tink.shaded.protobuf.LazyStringArrayList.emptyList

data class WomenCategory(
    val id: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?
)

data class WomenProgram(
    val id: Int,
    val category: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val description: String,
    val description_hi: String?,
    val description_ur: String?,
    val duration: String?,
    val duration_hi: String?,
    val duration_ur: String?,
    val mode: String?,
    val mode_hi: String?,
    val mode_ur: String?,
    val image: String?,

    val videos: List<WomenVideo>? = null,

    val resources: List<WomenResource>? = null,
    val support_contacts: List<SupportContact>? = null,
    val organizations: List<SupportOrganization>? = null,
    val schemes: List<GovernmentScheme>? = null,
    val faqs: List<WomenFAQ>? = null
)
data class WomenVideo(
    val id: Int, val title: String, val title_hi: String?, val title_ur: String?,
    val description: String?, val video_url: String, val source: String?
)

data class WomenResource(
    val id: Int, val title: String, val title_hi: String?, val title_ur: String?,
    val description: String?, val file: String, val resource_type: String?
)

data class SupportContact(
    val id: Int, val name: String, val name_hi: String?, val name_ur: String?,
    val phone: String, val email: String?, val is_emergency_helpline: Boolean, val address: String?
)

data class SupportOrganization(
    val id: Int, val name: String, val name_hi: String?, val name_ur: String?,
    val description: String?, val description_hi: String?, val description_ur: String?,
    val website: String?, val location: String?
)

data class GovernmentScheme(
    val id: Int, val title: String, val title_hi: String?, val title_ur: String?,
    val description: String?, val description_hi: String?, val description_ur: String?,
    val eligibility: String?, val eligibility_hi: String?, val eligibility_ur: String?,
    val benefits: String?, val benefits_hi: String?, val benefits_ur: String?,
    val official_link: String
)

data class WomenFAQ(
    val id: Int, val question: String, val question_hi: String?, val question_ur: String?,
    val answer: String, val answer_hi: String?, val answer_ur: String?
)