package com.example.taalimisafar.utils

import androidx.compose.runtime.mutableStateOf

// ✅ The Enum lives here now.
enum class AppLanguage(val code: String, val displayName: String) {
    NONE("en", "English Only"),
    HINDI("hi", "English + Hindi"),
    URDU("ur", "English + Urdu")
}

// ✅ The Manager lives here too.
object LanguageManager {
    var currentLanguage = mutableStateOf(AppLanguage.NONE)
}
object AppStrings {
    fun getLabel(label: String, language: AppLanguage): String {
        return when (language) {
            AppLanguage.HINDI -> when (label) {
                "Duration" -> "अवधि"
                "Eligibility" -> "योग्यता"
                "Mode" -> "मोड"
                "Financial Overview" -> "वित्तीय विवरण"
                "Govt Fees" -> "सरकारी फीस"
                "Pvt Fees" -> "निजी फीस"
                "Avg Salary" -> "औसत वेतन"
                "Admission Process" -> "प्रवेश प्रक्रिया"
                "Admission Date" -> "प्रवेश तिथि"
                "Course Overview" -> "कोर्स विवरण"
                else -> label
            }
            AppLanguage.URDU -> when (label) {
                "Duration" -> "دورانیہ"
                "Eligibility" -> "اہلیت"
                "Mode" -> "طریقہ کار"
                "Financial Overview" -> "مالیاتی جائزہ"
                "Govt Fees" -> "سرکاری فیس"
                "Pvt Fees" -> "پرائیویٹ فیس"
                "Avg Salary" -> "اوسط تنخواہ"
                "Admission Process" -> "داخلہ کا عمل"
                "Admission Date" -> "داخلہ کی تاریخ"
                "Course Overview" -> "کورس کا جائزہ"
                else -> label
            }
            else -> label // Default to English
        }
    }
}