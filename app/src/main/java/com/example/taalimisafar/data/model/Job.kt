package com.example.taalimisafar.data.model

data class Job(
    val id: Int,

    val title: String?,
    val organization_name: String?,
    val organization_logo: String?,
    val domain: String?,
    val job_type: String?,
    val job_shift: String?,
    val location: String?,
    val location_state_name: String?,
    val is_remote: Boolean = false,
    val experience_required: String?,
    val salary_or_payscale: String?,
    val total_vacancies: Int?,
    val skills_required: String?,
    val posted_date: String?,
    val category_sector: String?,
    val job_description: String?,
    val responsibilities: String?,
    val selection_process: String?,
    val application_mode: String?,
    val apply_link_or_email: String?,
    val last_date_to_apply: String?,
    val official_website: String?,
    val resume_required: Boolean = false,
    val portfolio_required: Boolean = false,
    val cover_letter_required: Boolean = false,


    val hr_name: String?,
    val contact_email: String?,
    val contact_phone: String?,

    val is_active: Boolean = true,
    val created_at: String?,
    val updated_at: String?,

    // --- Hindi Translations ---
    val title_hi: String?,
    val organization_name_hi: String?,
    val domain_hi: String?,
    val location_hi: String?,
    val experience_required_hi: String?,
    val salary_or_payscale_hi: String?,
    val skills_required_hi: String?,
    val job_description_hi: String?,
    val responsibilities_hi: String?,
    val selection_process_hi: String?,

    // --- Urdu Translations ---
    val title_ur: String?,
    val organization_name_ur: String?,
    val domain_ur: String?,
    val location_ur: String?,
    val experience_required_ur: String?,
    val salary_or_payscale_ur: String?,
    val skills_required_ur: String?,
    val job_description_ur: String?,
    val responsibilities_ur: String?,
    val selection_process_ur: String?
)