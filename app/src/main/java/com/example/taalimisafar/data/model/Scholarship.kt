package com.example.taalimisafar.data.model

import android.accessibilityservice.GestureDescription
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Required
import org.w3c.dom.Document

data class Scholarship(
    val id: Int,

    val category: Int?,

    @SerializedName("scholarship_type") val scholarship: Int?,

    val title: String,
    @SerializedName("provider_name") val providerName: String?,
    @SerializedName("scholarship_amount") val amount: String?,
    val description: String,
    val eligibility: String,
    @SerializedName("documents_required") val documentsRequired: String?,
    val benefits: String?,


    @SerializedName("title_hi") val titleHi: String?,
    @SerializedName("provider_name_hi") val providerNameHi: String?,
    @SerializedName("scholarship_amount_hi") val amountHi: String?,
    @SerializedName("description_hi") val descriptionHi: String?,
    @SerializedName("eligibility_hi") val eligibilityHi: String?,
    @SerializedName("documents_required_hi") val documentsRequiredHi: String?,
    @SerializedName("benefits_hi") val benefitsHi: String?,


    @SerializedName("title_ur") val titleUr: String?,
    @SerializedName("provider_name_ur") val providerNameUr: String?,
    @SerializedName("scholarship_amount_ur") val amountUr: String?,
    @SerializedName("description_ur") val descriptionUr: String?,
    @SerializedName("eligibility_ur") val eligibilityUr: String?,
    @SerializedName("documents_required_ur") val documentsRequiredUr: String?,
    @SerializedName("benefits_ur") val benefitsUr: String?,


    val status: String,
    @SerializedName("application_mode") val applicationMode: String?,
    val gender: String?,
    @SerializedName("min_income") val minIncome: String?,
    @SerializedName("max_income") val maxIncome: String?,

    @SerializedName("application_start_date") val applicationStartDate: String?,
    @SerializedName("last_date") val lastDate: String?,
    @SerializedName("correction_date") val correctionDate: String?,

    @SerializedName("official_link") val websiteLink: String?



)