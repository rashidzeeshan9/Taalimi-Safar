package com.example.taalimisafar.data.model

import com.google.gson.annotations.SerializedName

data class Course(
    val id: Int,

    // --- ENGLISH FIELDS ---
    @SerializedName("course_name") val courseName: String,
    @SerializedName("description") val description: String,
    @SerializedName("percentage_12th") val percentage_12th: String,
    @SerializedName("eligibility") val eligibility: String,

    @SerializedName("admission_process") val admission_process: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("age_limit") val age_limit: String,
    @SerializedName("admission_date") val admission_date: String,
    @SerializedName("qualification") val qualification: String,
    @SerializedName("application_mode") val application_mode: String,
    @SerializedName("average_course_fees_govt") val average_course_fees_govt: String,
    @SerializedName("average_course_fees_pvt") val average_course_fees_pvt: String,

    @SerializedName("curriculum") val curriculum: String,
    @SerializedName("future_scope") val future_scope: String,
    @SerializedName("average_salary") val average_salary: String,

    // --- HINDI FIELDS ---
    @SerializedName("course_name_hi") val courseName_hi: String?,
    @SerializedName("description_hi") val description_hi: String?,
    @SerializedName("percentage_12th_hi") val percentage_12th_hi: String?,
    @SerializedName("eligibility_hi") val eligibility_hi: String?,

    @SerializedName("admission_process_hi") val admission_process_hi: String?,
    @SerializedName("duration_hi") val duration_hi: String?,
    @SerializedName("age_limit_hi") val age_limit_hi: String?,
    @SerializedName("admission_date_hi") val admission_date_hi: String?,
    @SerializedName("qualification_hi") val qualification_hi: String?,
    @SerializedName("application_mode_hi") val application_mode_hi: String?,
    @SerializedName("average_course_fees_govt_hi") val average_course_fees_govt_hi: String?,
    @SerializedName("average_course_fees_pvt_hi") val average_course_fees_pvt_hi: String?,

    @SerializedName("curriculum_hi") val curriculum_hi: String?,
    @SerializedName("future_scope_hi") val future_scope_hi: String?,
    @SerializedName("average_salary_hi") val average_salary_hi: String?,


    // --- URDU FIELDS ---
    @SerializedName("course_name_ur") val courseName_ur: String?,
    @SerializedName("description_ur") val description_ur: String?,
    @SerializedName("percentage_12th_ur") val percentage_12th_ur: String?,
    @SerializedName("eligibility_ur") val eligibility_ur: String?,

    @SerializedName("admission_process_ur") val admission_process_ur: String?,
    @SerializedName("duration_ur") val duration_ur: String?,
    @SerializedName("age_limit_ur") val age_limit_ur: String?,
    @SerializedName("admission_date_ur") val admission_date_ur: String?,
    @SerializedName("qualification_ur") val qualification_ur: String?,
    @SerializedName("application_mode_ur") val application_mode_ur: String?,
    @SerializedName("average_course_fees_govt_ur") val average_course_fees_govt_ur: String?,
    @SerializedName("average_course_fees_pvt_ur") val average_course_fees_pvt_ur: String?,

    @SerializedName("curriculum_ur") val curriculum_ur: String?,
    @SerializedName("future_scope_ur") val future_scope_ur: String?,
    @SerializedName("average_salary_ur") val average_salary_ur: String?,
    )