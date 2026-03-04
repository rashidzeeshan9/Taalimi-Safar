package com.example.taalimisafar.data.model

data class InternshipCategory(
        val id: Int,
        val name: String,
        val name_hi: String?,
        val name_ur: String?,
        val image: String?
)

data class Internship(
        val id: Int,
        val category: Int?,
        val brochure_pdf: String?, // Changed from 'image' to match your Django FileField
        val image: String?,
        // --- Basic Details (Matched to cleaned Django Names) ---
        val title: String,
        val organization_name: String,
        val domain: String,
        val internship_type: String,
        val mode: String,
        val location: String,

        // --- Eligibility ---
        val who_can_apply: String,
        val course_required: String,
        val skills_required: String,
        val year_of_study: String,
        val age_limit: Int?,

        // --- Timeline & Benefits ---
        val duration: String,
        val start_date: String, // Django uses start_date instead of posted_date
        val last_date_to_apply: String,
        val created_at: String,
        val updated_at: String,
        val stipend_amount: String?,
        val certificate_provided: Boolean, // Boolean in Django
        val lor_provided: Boolean,
        val ppo_opportunity: Boolean,

        // --- Content ---
        val responsibilities: String,
        val tools_technologies: String,
        val learning_outcomes: String,
        val selection_process: String,

        // --- Application ---
        val application_mode: String,
        val application_link_or_email: String?,
        val resume_required: Boolean,
        val portfolio_required: Boolean,
        val college_id_required: Boolean,
        val cover_letter_required: Boolean,

        // --- Contact ---
        val hr_name: String?,
        val contact_email: String?,
        val contact_phone: String?,
        val official_website: String?,

        // --- Hindi Translations ---
        val title_hi: String?,
        val organization_name_hi: String?,
        val domain_hi: String?,
        val location_hi: String?,
        val who_can_apply_hi: String?,
        val course_required_hi: String?,
        val duration_hi: String?,
        val skills_required_hi: String?,
        val responsibilities_hi: String?,
        val tools_technologies_hi: String?,
        val learning_outcomes_hi: String?,
        val selection_process_hi: String?,

        // --- Urdu Translations ---
        val title_ur: String?,
        val organization_name_ur: String?,
        val domain_ur: String?,
        val location_ur: String?,
        val who_can_apply_ur: String?,
        val course_required_ur: String?,
        val duration_ur: String?,
        val skills_required_ur: String?,
        val responsibilities_ur: String?,
        val tools_technologies_ur: String?,
        val learning_outcomes_ur: String?,
        val selection_process_ur: String?
)