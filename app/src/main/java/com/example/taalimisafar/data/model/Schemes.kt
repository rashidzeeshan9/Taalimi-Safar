package com.example.taalimisafar.data.model

import org.w3c.dom.Document

data class SchemesCategory (
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val image: String?
    )
data class Schemes(
    val id: Int,
    val category: Int?,
    val image: String?,
    val scheme_image: String?,
    val title: String,
    val introduced_by: String,
    val state: String,
    val target_audience: String,
    val description: String,
    val benefits: String,
    val eligibility: String,
    val documents_required: String,
    val application_process: String,
    val helpline_number: String?,
    val brochure_pdf: String?,
    val official_link: String,
    val launch_date: String?,
    val last_date_to_apply: String?,
    val is_active: Boolean,

    val title_hi: String?,
    val introduced_by_hi: String?,
    val target_audience_hi: String?,
    val description_hi: String?,
    val benefits_hi: String?,
    val eligibility_hi: String?,
    val document_hi: Document?,
    val application_process_hi: String?,

    val title_ur: String?,
    val introduced_by_ur: String?,
    val target_audience_ur: String?,
    val description_ur: String?,
    val benefits_ur: String?,
    val eligibility_ur: String?,
    val document_ur: Document?,
    val application_process_ur: String?

)
